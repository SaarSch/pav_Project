type SLLNode {
  data:int
  next:SLLNode
}

simpleSLL(mut head:SLLNode, mut temp:SLLNode, mut temp2:SLLNode) -> () {
  
  example {
    [head==SLLNode7960847b && SLLNode7960847b.data==2 && SLLNode7960847b.next==null && temp==SLLNode34340fab && SLLNode34340fab.data==1 && SLLNode34340fab.next==null && temp2==SLLNode2aafb23c && SLLNode2aafb23c.data==1 && SLLNode2aafb23c.next==SLLNode34340fab && SLLNode34340fab.data==1 && SLLNode34340fab.next==null]
    -> head.data = 3; // [head==SLLNode7960847b && SLLNode7960847b.data==3 && SLLNode7960847b.next==null && temp==SLLNode34340fab && SLLNode34340fab.data==1 && SLLNode34340fab.next==null && temp2==SLLNode2aafb23c && SLLNode2aafb23c.data==1 && SLLNode2aafb23c.next==SLLNode34340fab && SLLNode34340fab.data==1 && SLLNode34340fab.next==null]
    -> head.next = temp; // [head==SLLNode7960847b && SLLNode7960847b.data==3 && SLLNode7960847b.next==SLLNode34340fab && SLLNode34340fab.data==1 && SLLNode34340fab.next==null && temp==SLLNode34340fab && SLLNode34340fab.data==1 && SLLNode34340fab.next==null && temp2==SLLNode2aafb23c && SLLNode2aafb23c.data==1 && SLLNode2aafb23c.next==SLLNode34340fab && SLLNode34340fab.data==1 && SLLNode34340fab.next==null]
    -> head.data = 9; // [head==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==SLLNode34340fab && SLLNode34340fab.data==1 && SLLNode34340fab.next==null && temp==SLLNode34340fab && SLLNode34340fab.data==1 && SLLNode34340fab.next==null && temp2==SLLNode2aafb23c && SLLNode2aafb23c.data==1 && SLLNode2aafb23c.next==SLLNode34340fab && SLLNode34340fab.data==1 && SLLNode34340fab.next==null]
    -> temp2.data = 5; // [head==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==SLLNode34340fab && SLLNode34340fab.data==1 && SLLNode34340fab.next==null && temp==SLLNode34340fab && SLLNode34340fab.data==1 && SLLNode34340fab.next==null && temp2==SLLNode2aafb23c && SLLNode2aafb23c.data==5 && SLLNode2aafb23c.next==SLLNode34340fab && SLLNode34340fab.data==1 && SLLNode34340fab.next==null]
  }
}
