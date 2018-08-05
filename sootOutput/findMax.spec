type SLLNode {
  data:int
  next:SLLNode
}

findMax(mut head:SLLNode) -> (max:int) {
  var t:SLLNode
  var i0:int

  example {
    [head==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null]
    -> t = head; // [head==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null && t==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null]
    -> max = head.data; // [head==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null && t==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null && max==9]
    -> i0 = t.data; // [head==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null && t==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null && max==9]
    -> t = t.next; // [head==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null && t==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null && max==9]
    -> i0 = t.data; // [head==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null && t==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null && max==9]
    -> t = t.next; // [head==SLLNode7960847b && SLLNode7960847b.data==9 && SLLNode7960847b.next==SLLNode61443d8f && SLLNode61443d8f.data==1 && SLLNode61443d8f.next==null && t==null && max==9]
  }
}
