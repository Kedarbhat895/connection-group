# connection-group
Spring boot based application to maintain hierarchical relation between Virtual nodes in a connection group.

There are virtual nodes in the system, we want to connect these virtual  nodes in the hierarchal fashion. 
System can have connection group on which various operations can be performed 

![image](https://github.com/user-attachments/assets/5d65c993-47ab-437e-9341-396c4a43d49e)


Requirements: 

1. User should be able to create new connection group. 
2. User should be able find the connection group by providing virtual node name.  From the above example if user specifies vnode6 then connection group #2 should be  returned.
3. Delete a virtual node
4. User should not be able to attach same virtual node to a different virtual node if it is already associated with a different connection group.


It has following api's:
Create Connection Group:

```
curl --location --request POST 'http://localhost:8080/connection-group' \
--header 'User-Agent: Apidog/1.0.0 (https://apidog.com)' \
--header 'Content-Type: application/json' \
--data-raw '{
    "connectionGroupName" : "group1",
    "rootNode": "rootNode2",
    "nodes" : ["child1", "child2", "child7"],
    "connectedComponents": [["rootNode2", "child1"], ["child1", "child2"], ["child2", "child7"] ]
}'
```

Get Connection Group with Virtual Nodes:
```
curl --location --request GET 'http://localhost:8080/connection-group/child2' \
--header 'User-Agent: Apidog/1.0.0 (https://apidog.com)'
```

Delete Virtual Nodes

```
curl --location --request DELETE 'http://localhost:8080/connection-group/child7' \
--header 'User-Agent: Apidog/1.0.0 (https://apidog.com)'
```
