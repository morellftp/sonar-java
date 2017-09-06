
abstract class A {

  boolean cond, cond2;

  private void methodA() throws ExA {
    if (cond) throw new ExA();
  }

  private void methodAB() throws ExA, ExB {
    if (cond) throw new ExA();
    else if (cond2) throw new ExB();
  }

  void test() {
    // FIXME ex should be removed see SONARJAVA-2446
    Object o = null; // flow@normal,ex {{Implies 'o' is null.}}
    try {
      methodA();  // flow@ex {{'ExA' is thrown.}}
    } catch (ExA e) {

    }
    o.toString(); // Noncompliant [[flows=normal,ex]]  flow@normal,ex {{'o' is dereferenced.}}
  }

  void test_multiple_ex_flows() {
    Object o = null; // flow@f1,f2 {{Implies 'o' is null.}}
    try {
      methodAB();  // flow@f1 {{'ExA' is thrown.}} flow@f2 {{'ExB' is thrown.}}
      o = new Object();
    } catch (ExA e) {

    } catch (ExB e) {

    }
    o.toString(); // Noncompliant [[flows=f1]]  flow@f1 {{'o' is dereferenced.}}
  }

  abstract void noBehavior() throws ExA, ExB;

  void test_method_with_no_behavior() {
    Object o = null; // flow@f1,f2 {{Implies 'o' is null.}}
    try {
      noBehavior();  // flow@f1 {{'ExA' is thrown.}} flow@f2 {{'ExB' is thrown.}}
      o = new Object();
    } catch (ExA e) {
      o.toString(); // Noncompliant [[flows=f1]]  flow@f1 {{'o' is dereferenced.}}
    } catch (ExB e) {
      o.toString(); // Noncompliant [[flows=f2]]  flow@f2 {{'o' is dereferenced.}}
    }
  }

  class ExA extends Exception {}
  class ExB extends Exception {}
}
