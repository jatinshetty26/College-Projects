import PipelineSimulator
import Instruction
import os
import sys

'''
1. Consider Floating and Doubles
2. Consider co-processor
3. Consider including config.txt file
4. Consider the number of cycles for each operation
'''


def main():
    print("Start")
    print("Setting all types of instructions into a dict")
    iparser = Instruction.InstructionParser()
    print("iparser : ",iparser)
    pipelinesim = PipelineSimulator.PipelineSimulator(iparser.parseFile("sample.txt"))
    config = iparser.parseConfigFile("config.txt")

    print("***************************************************")
    print("Summary so far: ")
    print("1. File Read")
    print("2. Parsed and determined type of instruction")
    print("3. Stored in a list of instructions")
    print("4. Created Registers")
    print("5. Created Main Memory")
    print("6. Added each instruction to the main memory")
    print("***************************************************")

    filename = "debug.txt"
    print("File name : ",filename)

    f = open(filename, 'w')

    print("Begining to Run")
    pipelinesim.run()
    print("Done Running")

    sys.stdout = f

if __name__ == "__main__":
    main()

