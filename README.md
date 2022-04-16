- Also need self address 
- First - create a root node - this wouldn't look for anyone but listening for new nodes - max 3
- Next create a node - with the target address:port and the node's name
- Pings the node.
- If we get a pong back we have a connection
- User gets a cli - Where they can query neighbours
- We create a folder, or if one is already there we query the goods and hold an array
- Next we can respond to queries or forward them, also we can initiate queries
<br>

## keywords for search:
- cli: search cat.png
- udp: search cat.png localhost:from localhost:origin
- udp: found cat.png localhost:founder -> start tcp listening thread 
- udp: ready cat.png -> start sending tcp packet


# Detailed Plan
## Node
### There are three core threads - beginning with a pre-processor to initialize values of the node
- CLI - All args go here -> TTL, command, IP, address...
- Conv-listener - Only updates last updated for neighbour nodes + creates new nodes
- Conv-manager - Checks every 2 seconds, initiates ping and pong


# Example of CLI
- SEARCH catPair.jpg
- PING localhost:8085