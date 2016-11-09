def get_ground_truth(ground_truth_path):
    ground_truth = dict()
    f = open(ground_truth_path, 'r')
    communityIdx = 0
    for line in f:
        isCommunity = False
        if line is None or len(line)==0:
            continue
        contents = line.split()
        for content in contents:
            content = content.strip()
            if len(content) == 0:
                continue
            nodeIdx = int(content)
            ground_truth[nodeIdx] = communityIdx

            isCommunity = True

        if isCommunity:
            communityIdx += 1
    f.close()

    return ground_truth

def evaluate_result(path, ground_truth):
    f = open(path, 'r')
    mis_classified_num, total_nodes_num = 0, 0
    for line in f:
        nodes = []
        if line is None or len(line)==0:
            continue
        contents = line.split()
        for content in contents:
            content = content.strip()
            if len(content) == 0:
                continue
            nodeIdx = int(content)
            nodes.append(nodeIdx)

        if len(nodes) == 0:
            continue

        votes = dict()
        for node in nodes:
            label = ground_truth[node]
            if label not in votes:
                votes[label] = 0
            votes[label] += 1

        winner_label, winner_votes = None, 0
        for label in votes.keys():
            votenum = votes[label]
            if votenum > winner_votes:
                winner_votes = votenum
                winner_label = label

        num_fail, total_num = 0, 0
        for node in nodes:
            label = ground_truth[node]
            if label != winner_label:
                num_fail += 1
            total_num += 1

        mis_classified_num += num_fail
        total_nodes_num += total_num

    f.close()

    mis_classified_rate = float(mis_classified_num)/total_nodes_num
    return mis_classified_rate


if __name__ == '__main__':
    from sys import argv
    from os.path import join
    if len(argv) < 2:
        print 'please input the data dir'

    data_dir = argv[1]
    ground_truth_path = join(data_dir, 'groundtruth.txt')
    normal_result_path = join(data_dir, 'output.txt')
    sample_result_path = join(data_dir, 'output.txt')
    ground_truth = get_ground_truth(ground_truth_path)
    normal_result = evaluate_result(normal_result_path, ground_truth)
    sample_result = evaluate_result(sample_result_path, ground_truth)

    print 'mis classified rate for normal algorithm: ', normal_result
    print 'mis classified rate for sampling algorithm: ', sample_result





