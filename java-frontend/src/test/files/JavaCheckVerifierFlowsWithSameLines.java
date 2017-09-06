class A {

  void f() {
    b.toString(); // Noncompliant [[flows=f1,f2]] {{error}}  flow@f1,f2  {{line4}}
    Object a = null; // flow@f1 {{f1}} flow@f2 {{f2}}
    Object b = new Object();  // flow@f1,f2 {{line6}}
  }
}
