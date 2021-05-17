Use java -cp bin/ "file name" to run the applications

The way that the input for my program is taken in is as follows:
arg[0] - table size 
arg[1] - collision scheme - type either linear, quadratic or chaining
arg[2] - input data file
arg[3] - number of search keys

e.g java -cp bin/ HashFunction 653 chaining cleaned_data.csv 400
