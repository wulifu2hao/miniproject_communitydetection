import json

# input: the path of input file
# Output: graph in adj list; num_nodes, num_edges
# Note: it assumes that at the beginning of the file there would be a line that contains graph num of nodes info in the format of:
#   # Nodes: <num of nodes> Edges: <num of edges>
#   After that each line that contains exactly two integers are consider an edge
# Usage: assuming your file is in the same directory of this file, you can use this function by doing 'from graph_loader import read_graph'
def read_graph(input_path):
    num_nodes, num_edges = 0, 0
    graph = []

    f = open(input_path,'r')
    for line in f:
        if line is None or len(line) == 0:
            continue
        contents = line.split()
        if len(contents) == 5:
            num_nodes = int(contents[2].strip())
            num_edges = int(contents[4].strip())
            graph = [[] for i in range(num_nodes)]
            continue
        if len(contents) != 2:
            continue
        try:
            nodeOne, nodeTwo = int(contents[0].strip()), int(contents[1].strip())
            graph[nodeOne].append(nodeTwo)
        except Exception, e:
            print contents


    f.close()

    return graph, num_nodes, num_edges

def dump_graph_json(output_path, graph, num_nodes, num_edges):
    f = open(output_path, 'w')
    obj = {
        'grpah':graph,
        'num_nodes':num_nodes,
        'num_edges':num_edges,
    }
    json.dump(obj, f)
    f.close()

def read_graph_json(input_path):
    f = open(input_path, 'r')
    obj = json.load(f)
    f.close()

    return obj['grpah'],obj['num_nodes'],obj['num_edges']

def get_simply_test_graph():
    graph = [
    [1,2],
    [0,2],
    [0,1,3],
    [2,4],
    [3,5,6],
    [4,6],
    [4,5],
    ]

    num_edges = 0
    for neighbours in graph:
        num_edges += len(neighbours)
    return graph, len(graph), num_edges

# you can use this program to convert a file that conforms to the format that can be understood by the read_graph function to json format
# so that you don't have to parse the input file every time when you run your program
# This would save you significant amount of time when processing input that is large
if __name__ == '__main__':
    from sys import argv
    from os.path import exists, splitext
    if len(argv) != 2:
        print 'please input the path for the file to be converted into json'

    input_path = argv[1]
    if not exists(input_path):
        print 'the file path you input doesnt exist'

    graph, num_nodes, num_edges = read_graph(input_path)

    filename, _ = splitext(input_path)
    json_file_path = filename+'.json'
    dump_graph_json(json_file_path, graph, num_nodes, num_edges)

