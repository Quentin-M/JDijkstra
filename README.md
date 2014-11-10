# JDijkstra
JDijkstra is a small project I worked on during my freshman in Polytech Tours (2012). Three different [Dijkstra algorithms](http://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) (shortest path finding)  are implemented :
* Mono-objective Dijkstra
* Simulated Bi-objective Dijkstra
* Bi-objective Dijkstra (with Pareto Frontier)

Mono-objective Dijkstra is a fast Dijkstra implementation which consider only one criteria (distance). It will stop once the destination node has been explored and return the path.

Simulated Bi-objective Dijstra uses a linear combination between distance and danger to try to find the best compromise between these two criteria. It will stop when the destination node is explored and return the path. We have to specify the weight of each criteria. For instance: 30% for distance & 70% for danger.

The third algorithm finds every non-dominated shortest paths consdering distance and danger at the same time. It returns a list of paths, using Pareto Frontier. This version is *really* slower.

A simple (and crappy) GUI is available as well as some samples (Paris's map).

Feel free to fork & do pull requests. I don't purport that this source-code is good, I did it during my freshman year in only 6 hours of work and I didn't work on it afterwards. Yet it can still be interesting for some people. Sorry about that ~
