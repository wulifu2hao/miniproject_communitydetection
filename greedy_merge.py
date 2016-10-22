from binary_search_tree import BinarySearchTree


# TODO: unfortunately we need to re-implement a heap which supports modify key.. we shall copy the implementation of heapq then modify on it

class Greedy_Merger:
    def __init__(self, graph, num_edges):
        self.graph = graph
        self.num_edges

    def init_Q_tree_list(self):
        self.delta_Q_tree_list = [BinarySearchTree() for i in range(len(graph))]

        # each node will have a BST
        for i in range(len(graph)):
            delta_Q_tree = self.delta_Q_treetree_list[i]
            degree_i = len(graph[i])
            for j in graph[i]:
                degree_j = len(graph[j])
                # the key of the bst if the index of the neighbour j
                # the value of each node is the initial delta_Q
                delta_Q_tree[j] = 0.5*self.num_edges-float(degree_i*degree_j)/((2*self.num_edges)**2)

    def init_Q_heap_list(self):
        pass

    def solve(self):
        self.init_Q_tree_list()

        # init delta_Q_heap_list, each heap will have delta_Q as key and the node index as value. in order to support modifying the key of an item we need a map from item to its position and a customized version of heap to do that
        # init dalta_Q_heap, each item would be the max element in each row
        # init a list

        # after init, we would do merging until we can't find any positive delta_Q from the dalta_Q_heap

        pass

