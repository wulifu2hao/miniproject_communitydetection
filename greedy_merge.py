# from binary_search_tree import BinarySearchTree
import graph_loader
from advanced_heap import Advance_heap, Heap_item
import logging

VERBOSE = False
def print_if_verbose(str):
    if VERBOSE:
        print str

class Greedy_Merger:
    def __init__(self, graph, num_edges):
        self.graph = graph
        self.num_nodes = len(graph)
        self.num_edges = num_edges

    def init_delta_Q(self):
        graph = self.graph

        # each element in self.delta_Q_map_list is a map from neighbour index j to the delta_Q_ij item, to enable fast lookup of a delta_Q item
        # note that this implementation is different from the original paper, in which a BST is used instead of a hashmap. Since I can't see any benefit of using a BST I decide to use a map
        self.delta_Q_map_list = [dict() for i in range(self.num_nodes)]
        # each element in self.delta_Q_heap_list is a max heap storing the delta_Q items. The key of the item is the delta_Q for (i, j) and the value is the index of j (the neighbour of node i)
        self.delta_Q_heap_list = [None for i in range(self.num_nodes)]

        # max_delta_Q_each_row keep tracks of the maximum deata_Q of each row
        max_delta_Q_each_row = [Heap_item(0, (i, 0)) for i in range(self.num_nodes)]

        self.ai_list = [0 for i in range(self.num_nodes)]

        # TODO: deal with the edge case when there is no neighbours for node i
        # which would leads to empty BST and empty heap
        for i in range(self.num_nodes):
            degree_i = len(graph[i])
            delta_Q_map = self.delta_Q_map_list[i]
            heap_item_list = [Heap_item(0, j) for j in graph[i]]

            for idx, j in enumerate(graph[i]):
                degree_j = len(graph[j])

                delta_Q = 0.5*self.num_edges-float(degree_i*degree_j)/((2*self.num_edges)**2)

                # the key of the heap is the dalta_Q
                # the value of each node is the index of the neighbour j
                heap_item_list[idx].key = delta_Q
                # the key of the map is the index of the neighbour j
                # the value of each node is the heap item
                delta_Q_map[j] = heap_item_list[idx]

            self.delta_Q_heap_list[i] = Advance_heap(heap_item_list)
            max_delta_Q_item = self.delta_Q_heap_list[i].peek_max()
            max_delta_Q_each_row[i].key = max_delta_Q_item.key
            max_delta_Q_each_row[i].value = (i, max_delta_Q_item.value)

            # assign the initial ai value to ai list
            ai = float(degree_i)/(2*self.num_edges)
            self.ai_list[i] = ai

        # we define an extra data structure to keep track of the max_delta_Q item of each row
        self.map_of_max_delta_Q_of_each_row = dict()
        for row in range(self.num_nodes):
            self.map_of_max_delta_Q_of_each_row[row] = max_delta_Q_each_row[row]

        self.heap_of_max_delta_Q_of_each_row = Advance_heap(max_delta_Q_each_row)

        self.result = [i for i in range(self.num_nodes)]


    def solve(self):
        self.init_delta_Q()
        self._validate_and_print_status('after initialization')

        # after init, we would do merging until we can't find any positive delta_Q from the dalta_Q_heap
        while not self.heap_of_max_delta_Q_of_each_row.is_empty():

            max_delta_Q_item = self.heap_of_max_delta_Q_of_each_row.pop_max()
            delta_Q, (i, j) = max_delta_Q_item.key, max_delta_Q_item.value
            # stop the process when delta_Q can no longer be positive
            # TODO: double check with the paper to make sure this is correct
            if delta_Q <= 0:
                break

            print ('start merging %d to %d, delta_Q=%f'%(i, j, delta_Q))
            # self._validate_and_print_status('start merging %d to %d'%(i, j))
            # since i is going to be merged with j, we remove it from this map also
            del self.map_of_max_delta_Q_of_each_row[i]

            # we are going to merge node i and j
            # in the meanwhile, we maintain a list of the index of the rows that have been modified
            modified_row_index_list = []

            # step 1: update row j
            # enumerate all the neighbours of node i
            for k in self.delta_Q_map_list[i]:
                if k == j:
                    # it's impossible to merge j to j, so we ignore this case
                    continue
                delta_Q_ik = self.delta_Q_map_list[i][k]
                if k in self.delta_Q_map_list[j]:
                    # if the a neighbour k is also neighbour of j, we add delta_Q_ik to delta_Q_jk, both in map and in heap
                    delta_Q_jk = self.delta_Q_map_list[j][k]
                    new_delta_Q_jk = delta_Q_ik.key + delta_Q_jk.key

                    # by doing this, the actual value of delta_Q_jk would be changed to new_delta_Q_jk. the heap will re-position this item; and the map will automatically change because it's just a map
                    self.delta_Q_heap_list[j].modify_key(delta_Q_jk, new_delta_Q_jk)
                else:
                    # else we insert a new item delta_Q_jk = delta_Q_ik - 2*aj*ak into both the map and heap
                    delta_Q_jk_key = delta_Q_ik.key - 2*self.ai_list[j]*self.ai_list[k]
                    delta_Q_jk = Heap_item(delta_Q_jk_key, k)
                    self.delta_Q_heap_list[j].push(delta_Q_jk)
                    self.delta_Q_map_list[j][k] = delta_Q_jk

            #  enumerate all the neighbours of node j
            for k in self.delta_Q_map_list[j]:
                if k in self.delta_Q_map_list[i]:
                    # if the neighbour k is also neighbour of i, we ignore it since it has been dealt with
                    continue

                # else we decrease delta_Q_jk by 2*ai*ak
                delta_Q_jk = self.delta_Q_map_list[j][k]
                if k == i:
                    # since i has been merged into j, we deal with it differently
                    new_delta_Q_jk = 0
                else:
                    new_delta_Q_jk = delta_Q_jk.key - 2*self.ai_list[i]*self.ai_list[k]

                self.delta_Q_heap_list[j].modify_key(delta_Q_jk, new_delta_Q_jk)

            # record that row j has been modified
            modified_row_index_list.append(j)

            # self._validate_and_print_status('after modifying row %d'%j)

            # step 2: update row i
            # the data in row i now can be safely removed
            # we purposely setting it to None instead of an initialized version of map and heap in order to expose bug if exists
            self.delta_Q_map_list[i] = None
            self.delta_Q_heap_list[i] = None

            # self._validate_and_print_status('remove data for row %d'%i)

            # step 3: update other rows which is connected to i or j
            # for each node (except i or j)
            for k in range(self.num_nodes):
                if k == i or k == j:
                    continue

                if self.delta_Q_map_list[k] is None:
                    # this node has been merged to another node
                    # ignore it
                    continue

                delta_Q_map_of_k = self.delta_Q_map_list[k]
                delta_Q_heap_of_k = self.delta_Q_heap_list[k]
                i_k_connected = i in delta_Q_map_of_k
                j_k_connected = j in delta_Q_map_of_k
                if not i_k_connected and not j_k_connected:
                    # this node is connected to neither i or j, ignore it
                    continue
                if i_k_connected and j_k_connected:
                    # this node is connected to both i and j
                    # we should increse delta_Q_kj and set delta_Q_ki to 0
                    delta_Q_ki = delta_Q_map_of_k[i]
                    delta_Q_kj = delta_Q_map_of_k[j]

                    new_delta_Q_kj = delta_Q_kj.key + delta_Q_ki.key
                    # increase delta_Q_kj
                    delta_Q_heap_of_k.modify_key(delta_Q_kj, new_delta_Q_kj)

                    # remove delta_Q_ki
                    delta_Q_heap_of_k.modify_key(delta_Q_ki, 0)
                    # TODO:will it be problematic to del it from the map in this way?
                    del delta_Q_map_of_k[i]
                elif i_k_connected:
                    # this node is connected to i but not j
                    # we should insert a new delta_Q_kj and remove delta_Q_ki
                    delta_Q_ki = delta_Q_map_of_k[i]

                    # insert a new delta_Q_kj
                    delta_Q_kj_key = delta_Q_ki.key - 2*self.ai_list[j]*self.ai_list[k]
                    delta_Q_kj = Heap_item(delta_Q_kj_key, j)
                    delta_Q_heap_of_k.push(delta_Q_kj)
                    delta_Q_map_of_k[j] = delta_Q_kj

                    # remove delta_Q_ki
                    delta_Q_heap_of_k.modify_key(delta_Q_ki, 0)
                    # TODO:will it be problematic to del it from the map in this way?
                    del delta_Q_map_of_k[i]

                else:
                    # this node is connected to j but not i
                    # we will decrease delta_Q_kj
                    delta_Q_kj = delta_Q_map_of_k[j]
                    new_delta_Q_kj = delta_Q_kj.key - 2*self.ai_list[i]*self.ai_list[k]

                    delta_Q_heap_of_k.modify_key(delta_Q_kj, new_delta_Q_kj)

                # if the program manages to proceed to this place, this row must have been modified
                modified_row_index_list.append(k)

            # self._validate_and_print_status('updated all related rows %s'%str(modified_row_index_list))

            # step 4: update self.heap_of_max_delta_Q_of_each_row because there are some rows that have been modified and their max delta_Q have changed
            for row in modified_row_index_list:
                if row not in self.map_of_max_delta_Q_of_each_row:
                    print 'a node %d which has been merged is modified. This must be a bug!'%row
                    self._validate_and_print_status('bug found: ')
                    continue

                previous_max_delta_Q_item_of_this_row = self.map_of_max_delta_Q_of_each_row[row]
                (_, neighbour_index) = previous_max_delta_Q_item_of_this_row.value
                previous_delta_Q_value = previous_max_delta_Q_item_of_this_row.key

                if len(self.delta_Q_map_list[row]) == 0 or self.delta_Q_heap_list[row].is_empty() or self.delta_Q_heap_list[row].peek_max().key <= 0:
                    # there is no more (valuable) connection from this row to other, it's impossible that we can find something to merge with it in the future
                    # we should remove it from map_of_max_delta_Q_of_each_row

                    # for debugging
                    if len(self.delta_Q_map_list[row]) == 0:
                        print_if_verbose('deleting row %d because self.delta_Q_map_list[row] is empty'%row)
                    elif self.delta_Q_heap_list[row].is_empty():
                        print_if_verbose('deleting row %d because self.delta_Q_heap_list[row] is empty'%row)
                    else:
                        too_small_key = self.delta_Q_heap_list[row].peek_max().key
                        print_if_verbose('deleting row %d delta_Q %f is too small'%(row, too_small_key))

                    self.heap_of_max_delta_Q_of_each_row.modify_key(previous_max_delta_Q_item_of_this_row, 0)
                    del self.map_of_max_delta_Q_of_each_row[row]
                    continue

                # if the program reaches this point, it means that this node can still merge with other node
                # we try to get the best option of merging
                new_max_delta_Q_item_of_this_row = self.delta_Q_heap_list[row].peek_max()

                if neighbour_index == new_max_delta_Q_item_of_this_row.value and previous_delta_Q_value == new_max_delta_Q_item_of_this_row.key:
                    # this row's best merging option has not changed, and the delta_Q remain the same, this is the only case that we don't need to touch the existing max_delta_Q_of_each_row
                    continue

                # if the program reaches this point, it means that at least the max_delta_Q_value has changed
                # in this case, we simply remove the old one and insert the new one
                print_if_verbose('modifying heap_of_max_delta_Q_of_each_row for row %d'%row)
                self.heap_of_max_delta_Q_of_each_row.modify_key(previous_max_delta_Q_item_of_this_row, 0)
                new_max_delta_Q_item_to_add = Heap_item(new_max_delta_Q_item_of_this_row.key, (row, new_max_delta_Q_item_of_this_row.value))
                self.heap_of_max_delta_Q_of_each_row.push(new_max_delta_Q_item_to_add)
                self.map_of_max_delta_Q_of_each_row[row] =new_max_delta_Q_item_to_add

            # self._validate_and_print_status('updated heap_of_max_delta_Q_of_each_row')

            # step 5: update the ai list
            self.ai_list[j] += self.ai_list[i]
            self.ai_list[i] = 0

            # record down that the merge is successful
            if self.result[i] != i:
                err_msg =  'node %d has been merged another node % but now we are merging it to % again, something is wrong!' % (i, self.result[i], j)
                raise ValueError(err_msg)
            self.result[i] = j
            self._validate_and_print_status('finish merging %d to %d'%(i, j))

        self.print_result()

    def print_result(self):

        for i in range(self.num_nodes):
            cur_node, ancestor = i, None
            while True:
                parent = self.result[cur_node]
                if parent == cur_node:
                    # we have reach the last node of this group
                    ancestor = parent
                    break
                cur_node = parent
            cur_node = i
            while True:
                parent = self.result[cur_node]
                if parent == ancestor:
                    # all parent of the nodes along this 'chain' should all have been updated to the ancestor
                    break
                self.result[cur_node] = ancestor
                cur_node = parent

        groups = dict()
        for i in range(self.num_nodes):
            ancestor = self.result[i]
            if ancestor not in groups:
                groups[ancestor] = []
            groups[ancestor].append(i)

        print 'result: '
        count = 1
        for key in groups:
            group = groups[key]
            print 'group %d: %s'%(count, str(group))
            count += 1


    def _validate_and_print_status(self, current_stage):
        delta_Q_matrix = [[0 for j in range(self.num_nodes)] for i in range(self.num_nodes)]
        for i in range(self.num_nodes):
            if self.delta_Q_map_list[i] is None:
                continue
            for neighbour_idx in self.delta_Q_map_list[i]:
                delta_Q_item = self.delta_Q_map_list[i][neighbour_idx]
                delta_Q_matrix[i][neighbour_idx] = delta_Q_item.key

        matrix_str = 'delta_Q_matrix:\n'
        for row in delta_Q_matrix:
            matrix_str += '%s\n'%str(row)

        delta_Q_heap_list_status = [None for i in range(self.num_nodes)]
        for i in range(self.num_nodes):
            if self.delta_Q_heap_list[i] is None or self.delta_Q_heap_list[i].is_empty():
                continue
            delta_Q_heap_list_status[i] = self.delta_Q_heap_list[i].peek_max().value
        delta_Q_heap_list_status_str = 'delta_Q_heap_list_status_str\n%s\n'%(str(delta_Q_heap_list_status))

        max_delta_Q_of_each_row_list = [None for i in range(self.num_nodes)]
        for row in self.map_of_max_delta_Q_of_each_row:
            item = self.map_of_max_delta_Q_of_each_row[row]
            max_delta_Q_of_each_row_list[row] = item.value[1]
        max_delta_Q_of_each_row_str = 'max_delta_Q_of_each_row\n%s\n'%str(max_delta_Q_of_each_row_list)

        print_if_verbose(current_stage+'\n'+matrix_str+delta_Q_heap_list_status_str+max_delta_Q_of_each_row_str)

if __name__ == '__main__':
    for i in range(5):
        print ''.join(['-']*50)

    simple_graph, _, num_edges = graph_loader.get_simply_test_graph()
    greedy_merger = Greedy_Merger(simple_graph, num_edges)
    greedy_merger.solve()



