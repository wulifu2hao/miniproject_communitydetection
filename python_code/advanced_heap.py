"""
    Advance_heap is a simple customized version of heap
    which supports heapify, push, pop_max, peek_max, modify_key
    note that this data structure may still undergo changes as I implement the greedy merge algorithm
"""

class Heap_item:
    def __init__(self, key, value):
        self.key = key
        self.value = value
        self.position = 0

class Advance_heap:

    def __init__(self, heap_item_list=[]):
        self.heap_item_list = heap_item_list
        self.heapify()

    def push(self, item):
        self.heap_item_list.append(item)
        item.position = len(self.heap_item_list)-1
        self._siftdown(0, item.position)

    def is_empty(self):
        return len(self.heap_item_list) == 0

    def peek_max(self):
        return self.heap_item_list[0]

    def pop_max(self):
        lastelt = self.heap_item_list.pop()
        if not self.is_empty():
            returnitem = self.heap_item_list[0]
            self.heap_item_list[0] = lastelt
            lastelt.position = 0
            self._siftup(0)
        else:
            returnitem = lastelt
        return returnitem

    def heapify(self):
        n = len(self.heap_item_list)
        for i in reversed(xrange(n//2)):
            self._siftup(i)
        for i in range(len(self.heap_item_list)):
            self.heap_item_list[i].position = i

    def modify_key(self, item, new_key):
        if new_key == item.key:
            return

        position = item.position
        old_key = item.key
        item.key = new_key

        if new_key < old_key:
            # we are decreasing the key of this item
            self._siftup(position)
        else:
            self._siftdown(0, position)

    def _siftdown(self, startpos, pos):
        # get the item which needs to be bubble up
        newitem = self.heap_item_list[pos]

        while pos > startpos:
            parentpos = (pos - 1) >> 1
            parent = self.heap_item_list[parentpos]
            # print 'parent key', parent.key, 'newitem key', newitem.key
            if newitem.key > parent.key:
                # if its key is larger than its parent, swap them
                # print newitem.key, 'larger than parent', parent.key
                self.heap_item_list[pos] = parent
                parent.position = pos
                pos = parentpos
                continue
            break
        self.heap_item_list[pos] = newitem
        newitem.position = pos

    def _siftup(self, pos):
        endpos = len(self.heap_item_list)
        startpos = pos
        newitem = self.heap_item_list[pos]
        # Bubble up the smaller child until hitting a leaf.
        childpos = 2*pos + 1    # leftmost child position
        while childpos < endpos:
            # Set childpos to index of smaller child.
            rightpos = childpos + 1
            if rightpos < endpos:
                left_child, right_child = self.heap_item_list[childpos], self.heap_item_list[rightpos]
                if left_child.key < right_child.key:
                    childpos = rightpos
            # Move the larger child up.
            child_item = self.heap_item_list[childpos]
            self.heap_item_list[pos] = child_item
            child_item.position = pos
            pos = childpos
            childpos = 2*pos + 1
        # The leaf at pos is empty now.  Put newitem there, and bubble it up
        # to its final resting place (by sifting its parents down).
        self.heap_item_list[pos] = newitem
        newitem.position = pos
        self._siftdown(startpos, pos)

    def _get_random_item(self):
        if len(self.heap_item_list) == 0:
            return None
        random_index = randint(0, len(self.heap_item_list)-1)
        return self.heap_item_list[random_index]

    def _print_heap_item_list(self):
        keys = [(item.key, item.position) for item in self.heap_item_list]
        print keys

def _assert_sorted(lst, test_name):
    if len(lst) == 0:
        return

    last_item = lst[0]
    for i in range(1, len(lst)):
        cur_item = lst[i]
        if cur_item > last_item:
            error_name = '%s fails: %s' %(test_name, str(lst))
            raise ValueError(error_name)

    return

def _heap_sort(heap):
    res = []
    while not heap.is_empty():
        item = heap.pop_max()
        res.append(item.key)
    return res

def _test_sanity_check():

    # Simple sanity test
    data = [1, 3, 5, 7, 9, 2, 4, 6, 8, 0]

    heap_item_list = [Heap_item(i, str(i)) for i in data]
    heap = Advance_heap(heap_item_list)

    for i in range(randint(0,10)):
        # modify key for a few elements
        random_item = heap._get_random_item()
        random_new_key = randint(0, 100)

        heap.modify_key(random_item, random_new_key)

    # heap._print_heap_item_list()

    _assert_sorted(_heap_sort(heap), 'sanity check')

def _test_push():
    test_heap = Advance_heap()
    test_heap.push(Heap_item(1,'1'))
    test_heap.push(Heap_item(2,'1'))
    test_heap.push(Heap_item(3,'1'))

    _assert_sorted(_heap_sort(test_heap), 'sanity check')


# This program is only a sanity test to make sure that the heap works
if __name__ == "__main__":
    from random import randint

    _test_sanity_check()
    _test_push()


