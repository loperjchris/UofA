# PA5-GraphAlg
Starter code for PA5.  See the PA5 writeup in the Writeups repository for the github assignment link.
Do NOT clone this repository. Download the assignment through the github assignment link.

See https://github.com/Spr19CS210/PA-Drill-Section-Writeups/tree/master/PA5-GraphAlgPerf

big11.mtx file with TIME command:
test 1:
heuristic: cost = 3.3969307170000005, 2 milliseconds
mine: cost = 1.3566775349999998, 5928 milliseconds
backtrack: cost = 1.3566775349999998, 59074 milliseconds

test 2:
heuristic: cost = 3.3969307170000005, 2 milliseconds
mine: cost = 1.3566775349999998, 6185 milliseconds
backtrack: cost = 1.3566775349999998, 42720 milliseconds

test 3:
heuristic: cost = 3.3969307170000005, 2 milliseconds
mine: cost = 1.3566775349999998, 6596 milliseconds
backtrack: cost = 1.3566775349999998, 46926 milliseconds

A heuristic approach to the traveling salesman problem is faster than a backtracking approaching because the heuristic approach starts at a node then chooses one edge (one city to travel to), goes to the node on the other end of that edge and continues moving forward never going backward so it only traverses the graph in one single way rather than traversing all possiblities as backtracking would. The MINE algorithm in my program functions by combining the heuristic and backtracking approach. The MINE algorithm using the heuristic approach of following the edges with the least cost and using the cost of that trip as the upper limit. Then the algorithm uses that limit when using recursive backtracking to prune out the travel paths that would end up costing more than the cost of the heuristic approach.
