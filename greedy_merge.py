from binary_search_tree import BinarySearchTree
from advanced_heap import Advance_heap

class Greedy_Merger:
    def __init__(self, graph, num_edges):
        self.graph = graph
        self.num_nodes = len(graph)
        self.num_edges

    def init_delta_Q(self):
        graph = self.graph
        self.delta_Q_tree_list = [BinarySearchTree() for i in range(self.num_nodes)]
        self.delta_Q_heap_list = [None for i in range(self.num_nodes)]

        # each node will have a BST
        for i in range(self.num_nodes):
            degree_i = len(graph[i])
            delta_Q_tree = self.delta_Q_treetree_list[i]
            heap_item_list = [Heap_item(0, j) for j in graph[i]]

            for j in graph[i]:
                degree_j = len(graph[j])

                delta_Q = 0.5*self.num_edges-float(degree_i*degree_j)/((2*self.num_edges)**2)

                # the key of the bst if the index of the neighbour j
                # the value of each node is the initial delta_Q
                delta_Q_tree[j] = delta_Q
                # the key of the heap is the dalta_Q
                # the value of each node is the index of the neighbour j
                heap_item_list[j].key = delta_Q

            self.delta_Q_heap_list[i] = Advance_heap(heap_item_list)

    def solve(self):
        self.init_delta_Q()

        # init delta_Q_heap_list, each heap will have delta_Q as key and the node index as value. in order to support modifying the key of an item we need a map from item to its position and a customized version of heap to do that
        # init dalta_Q_heap, each item would be the max element in each row
        # init a list

        # after init, we would do merging until we can't find any positive delta_Q from the dalta_Q_heap

        pass

