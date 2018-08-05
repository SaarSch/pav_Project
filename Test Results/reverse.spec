type SLLNode {
  data:int
  next:SLLNode
}

reverse(mut head:SLLNode) -> (result:SLLNode) {
  var t:SLLNode

  example {
    [head==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null]
    -> result = null; // [head==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null && result==null]
    -> t = head.next; // [head==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==SLLNode61443d8f && SLLNode61443d8f.data==1 && result==null && t==SLLNode61443d8f && SLLNode61443d8f.data==1]
    -> head.next = result; // [head==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==null && result==null && t==SLLNode61443d8f && SLLNode61443d8f.data==1]
    -> result = head; // [head==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==null && result==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==null && t==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null]
    -> head = t; // [head==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null && result==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==null && t==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null]
    -> t = head.next; // [head==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null && result==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==null && t==null]
    -> head.next = result; // [head==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==SLLNode7960847b && SLLNode7960847b.data==9 && result==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==null && t==null]
    -> result = head; // [head==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==SLLNode7960847b && SLLNode7960847b.data==9 && result==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==null && t==null]
    -> head = t; // [head==null && result==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==SLLNode7960847b && SLLNode7960847b.data==9 && t==null]
  }
}
