"""
class Olar:
	i = None
	def __init__(self):
		self.i = 5 
	@property
	def f(self):
		return "hello world"	

a = Olar()
print(a.f())
print(a.i)
"""

class Instruction(object):
    def __init__(self, **input):
        self.result = None
        
        self.source1RegValue = None 
        self.source2RegValue = None
        self.values = {
                       'op': None,
                       'dest': None,
                       's1': None,
                       's2': None,
                       'immed': None,
                       'target': None
        }
        self.controls = {'aluop'   : None,
                         'regRead' : None,
                         'regWrite': None,
                         'readMem' : None,
                         'writeMem': None, }

        for key in input:
            print("key : ",key)
            if key in self.values.keys():
                print("Adding to values")
                self.values[key] = input[key]
            else:
                print("Adding to controls")
                self.controls[key] = input[key]
        print("self.values : ", self.values)
        print("self.controls : ", self.controls)

    @property
    def op(self):
        """ Get this Instruction's name """
        return self.values['op']
    
    @property
    def dest(self):
        """ Get this Instruction's destination register """
        return self.values['dest']
    
    @property
    def s1(self):
        """ Get this Instruction's first source register """
        return self.values['s1']
    
    @property
    def s2(self):
        """ Get this Instruction's second source register """
        return self.values['s2']
    
    @property
    def immed(self):
        """ Get this Instruction's immediate value """
        return self.values['immed']
    
    @property
    def target(self):
        """ Get this Instruction's target value """
        return self.values['target']
    
    @property
    def aluop(self):
        """ Get this Instruction's control to decide an alu operation """
        return self.controls['aluop']
    
    @property
    def regRead(self):
        """ Get this Instruction's control to decide to read a register"""
        return self.controls['regRead']
    
    @property
    def regWrite(self):
        """ Get this Instruction's control to decide to write a register """
        return self.controls['regWrite']
    
    @property
    def readMem(self):
        """ Get this Instruction's control to decide to read memory """
        return self.controls['readMem']
    
    @property
    def writeMem(self):
        """ Get this Instruction's control to decide to write memory """
        return self.controls['writeMem']
    
    
    def __str__(self):
        str = "%s\t%s %s %s %s %s" % (self.values['op'],
                                  self.values['dest'] if self.values['dest'] else "",
                                  self.values['s1'] if self.values['s1'] else "",
                                  self.values['s2'] if self.values['s2'] else "",
                                  self.values['immed'] if self.values['immed'] else "",
                                  self.values['target'] if self.values['target'] else "")
        return str
    
    def __repr__(self):
        return repr(self.values)
        
class Nop(Instruction):
    pass
#nop singleton
Nop = Nop()

class InstructionParser(object):
    def __init__(self):
        print("Initializing InstructionParser")
        self.instructionSet = {
            'rtype': ['add', 'sub', 'and', 'or', 'jr', 'nor', 'slt'],
            'itype': ['addi', 'subi', 'ori', 'bne', 'beq', 'lw', 'sw'],
            'jtype': ['j']
        }
        print("instructionSet : ",self.instructionSet)

    def parseFile(self, filename):
        with open(filename) as f:
            data = list(filter((lambda x: x != '\n'), f.readlines()))

            print("the file contents : ",data)
            instructions = [self.parse(a.replace(',',' ')) for a in data]
            return instructions

    def parseConfigFile(self,filename):
        print("Start Config file parsing")
        with open(filename) as f:
            data = list(filter((lambda x: x != '\n'), f.readlines()))
            print("the file contents : ", data)
            mainDict = {}

            for theLine in data:
                try:
                    theOperation = str(theLine.split(":").__getitem__(0).strip())
                    theCycles = int(theLine.split(":").__getitem__(1).split(",").__getitem__(0).strip())
                    isPipelined = str(theLine.split(":").__getitem__(1).split(",").__getitem__(1).replace("\\n", "").strip())
                    eachDict = {}
                    eachDict = {theOperation: {'cycles': theCycles, 'isPipelined': isPipelined}}
                    mainDict.update(eachDict)
                except IndexError:
                    theCycles = int(theLine.split(":").__getitem__(1).replace("\\n", "").strip())
                    eachDict = {}
                    eachDict = {theOperation: {'cycles': theCycles}}
                    mainDict.update(eachDict)
                    continue
        print(mainDict)
        return mainDict


    def parse(self, s):
        s = s.split()
        
        instr = s[0]
        print("The Operation : ",instr)
        
        if instr in self.instructionSet['rtype']:
            print("Its TYPE Rself.createRTypeInstruction(s)")
            return self.createRTypeInstruction(s)
        elif instr in self.instructionSet['itype']:
            print("ITS Type I self.createRTypeInstruction(s)")
            return self.createITypeInstruction(s)    
        elif instr in self.instructionSet['jtype']:
            print("Its Type J self.createRTypeInstruction(s)")
            return self.createJTypeInstruction(s)
        else:
            print("self.createRTypeInstruction(s) : ", self.createRTypeInstruction(s))
            raise ParseError("Invalid parse instruction")

    #TODO should be figuring out controls dynamically based on the op
    def createRTypeInstruction(self, s):
        if s[0] == "jr":
            return Instruction(op=s[0], s1 = s[1], regRead = 1, aluop=1)
        return Instruction(op=s[0], dest=s[1], s1=s[2], s2=s[3], regRead=1, regWrite=1, aluop=1)

    def createITypeInstruction(self, s):
        print("Checking MemRead and Write. The current Operation is : ",s[0])
        memread = s[0] == "lw"
        memwrite = s[0] == "sw"
        print("memread : ",memread)
        print("memwrite : ",memwrite)

        if (memread or memwrite):
            print("The MEM read/write is TRUE")
            import re
            regex = re.compile("(\d+\(?\$r\d+\)?)|(\d+)|(\$r\d+)")
            #regex = re.compile("((\d+)*\(?\$r\d+\)?)|(\d+)|(\$r\d+)")
            #regex = re.compile("(\d+)\((\$r\d+)\)")
            #regex = re.compile("(\d+)|(\$r\d+)")
            match = regex.match(s[2])
            print("The match : ",match)
            immedval = match.group(2)
            print("immedval : ",immedval)
            if immedval is None:
                immedval = 0

            sval = match.group(3)
            print("sval : ",sval)

            if s[0] == "lw" :
                print("Here since the OP is lw")
                return Instruction(op=s[0], dest = s[1], s1=sval, immed = immedval, regRead = 1,regWrite = 1, aluop=1,  readMem = 1)
            else :
                return Instruction(op=s[0],  s1 = s[1], s2=sval,immed = immedval, regRead = 1, aluop=1, writeMem = 1)

        if ( s[0] == 'bne' or s[0] == 'beq') :
            print("Here since its a branch OP")
            return Instruction(op=s[0], s1=s[1] , s2= s[2], immed = s[3], regRead = 1, aluop = 1)
        return Instruction(op=s[0], dest=s[1], s1=s[2], immed=s[3], regRead=1, regWrite=1, aluop=1)

    def createJTypeInstruction(self, s):
        return Instruction(op=s[0], target=s[1])

class ParseError(Exception):
    def __init__(self, value):
        self.value = value
    def __str__(self):
        return repr(self.value)