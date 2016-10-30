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
    max_node_idx = 0

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
            max_node_idx = max(max_node_idx, max(nodeOne, nodeTwo))
            graph[nodeOne].append(nodeTwo)
        except Exception, e:
            print 'error',e, contents


    f.close()

    if num_nodes != max_node_idx+1:
        print 'num nodes is actually', max_node_idx+1
    return graph, num_nodes, num_edges

def read_graph_nasty_input(input_path):
    num_nodes, num_edges = 0, 0
    graph = []
    node_idx_map = dict()
    fake_node_idx_used = 0

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
            if nodeOne not in node_idx_map:
                node_idx_map[nodeOne] = fake_node_idx_used
                fake_node_idx_used += 1
            if nodeTwo not in node_idx_map:
                node_idx_map[nodeTwo] = fake_node_idx_used
                fake_node_idx_used += 1

            graph[node_idx_map[nodeOne]].append(node_idx_map[nodeTwo])
        except Exception, e:
            print 'error',e, contents


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

def get_simple_test_graph(num_group, group_size):
    from random import randint

    num_nodes = num_group * group_size
    graph = [[] for i in range(num_nodes)]
    for group_idx in range(num_group):
        for node_idx in range(group_size):
            node_real_idx = group_idx*group_size + node_idx
            for neighbour_idx in range(group_size):
                if node_idx == neighbour_idx:
                    continue
                neighbour_real_idx = group_idx*group_size + neighbour_idx
                graph[node_real_idx].append(neighbour_real_idx)

    for i in range(num_group-1):
        # add an connection between group i and group i+1
        # node_idx_group_1 = randint(0, group_size-1)
        # node_idx_group_2 = randint(0, group_size-1)
        node_idx_group_1 = 0
        node_idx_group_2 = 0
        node_real_idx_1 = i * group_size + node_idx_group_1
        node_real_idx_2 = (i+1) * group_size + node_idx_group_2
        graph[node_real_idx_1].append(node_real_idx_2)
        graph[node_real_idx_2].append(node_real_idx_1)

    num_edges = (num_group-1) + (group_size-1) * num_nodes
    return graph, len(graph), num_edges

def _test_get_simple_test_graph():
    graph, _, num_edges = get_simple_test_graph(3,4)
    print graph

# you can use this program to convert a file that conforms to the format that can be understood by the read_graph function to json format
# so that you don't have to parse the input file every time when you run your program
# This would save you significant amount of time when processing input that is large
if __name__ == '__main__':
    from sys import argv
    from os.path import exists, splitext
    if len(argv) < 2:
        print 'please input the path for the file to be converted into json'

    input_path = argv[1]
    if not exists(input_path):
        print 'the file path you input doesnt exist'

    is_input_dirty = False
    if len(argv) > 2 and argv[2] == 'dirty':
        is_input_dirty = True

    if is_input_dirty:
        graph, num_nodes, num_edges = read_graph_nasty_input(input_path)
    else:
        graph, num_nodes, num_edges = read_graph(input_path)

    filename, _ = splitext(input_path)
    json_file_path = filename+'.json'
    dump_graph_json(json_file_path, graph, num_nodes, num_edges)
