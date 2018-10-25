import com.sun.deploy.Environment
import com.sun.org.apache.xpath.internal.operations.Bool
import java.lang.IllegalArgumentException
import java.util.*

fun main(args: Array<String>) {


    //numbers()
    //characters()
    //functions()
    //kotlinTutorial()

    functionalKotlinBook()
}

fun functionalKotlinBook() {

    // chapter 1
    chapter1()
}

fun chapter1() {
    //-----------------------------------------
    // classes
    //-----------------------------------------
    chapter1_example1()

    //-----------------------------------------
    // inheritance
    //-----------------------------------------
    chapter1_example2()

    //-----------------------------------------
    // abstract Classes
    //-----------------------------------------
    // difference between open and abstract
    // Both modifiers let us extend a class,
    // but open lets us instantiate while abstract does not.
    chapter1_example3()

    //-----------------------------------------
    // interfaces
    //-----------------------------------------
    // - In Kotlin, a class can't extend two classes at the same time.
    //   However, it can extend many interfaces.
    // - In an interface, all methods are open and a method with no
    //   implementation doesn't need an abstract modifier:

    chapter1_example4()

    //-----------------------------------------
    // what are the differences between an open/abstract class and an interface?
    //-----------------------------------------
    /*

    Let's start with the following similarities:

        - Both are types.
          In our example, Cupcake has an is-a relationship with BakeryGood
          and has an is-a relationship with Bakeable.

        - Both define behaviors as methods.

        - Although open classes can be instantiated directly,
          neither abstract classes nor interfaces can.

    Now, let's look at the following differences:

        - A class can extend just one class (open or abstract),
          but can extend many interfaces.

        - An open/abstract class can have constructors.

        - An open/abstract class can initialize its own values.An interface's
          values must be initialized in the classes that extend the interface.

        - An open class must declare the methods that can be overridden as open.
          An abstract class could have both open and abstract methods.
    */

    //-----------------------------------------
    // when to use open/abstract class and an interface?
    //-----------------------------------------

    /*
    My recommendation is that you should always start with an interface.
    Interfaces are more straightforward and cleaner;
    they also allow a more modular design.
    In the case that data initialization/constructors are needed,
    move to abstract/open.
    */


    //-----------------------------------------
    // Objects
    //-----------------------------------------

    // Object expressions don't need to extend any type

    val exp = object {

        val prop = "prop value"

        fun method(): Int {
            println("from an object expression")
            return 1
        }
    }

    val i = "${exp.method()} ${exp.prop}"
    println(i)

    // There is one restriction—object expressions without type can be used
    // only locally, inside a method, or privately, inside a class

    //In this case, the prop value can't be accessed.
    val outer = Outer()

    //Compilation error: Unresolved reference: prop
    /*
    println("prop : ${outer.exp.prop} " +
             "fun: ${outer.exp.function()}")
             */


    //An object can also have a name. This kind of object is called an
    // object declaration

    val bakeable = object : Bakeable {}
    Oven.process(bakeable)
//Objects are singletons; you don't need to instantiate Oven to use it.
// Objects also can extend other types:
    println(BakeableObject.bake())

    // Companion objects
    //Objects declared inside a class/interface can be marked as companion
    // objects.

    val person1 = PersonCompanion.person1()
    println(person1.getName())
    val person2 = PersonCompanion.person2()
    println(person2.getName())
    val person3 = PersonCompanion("mo" , "gaber")
    println(person3.getName())


    val factory = PersonCompanion.Factory
    factory.person1()
    factory.person2()

    val x: Array<Int> = arrayOf(1,2,3)
    val t = x.joinToString()
    println(t)


    fun toJSON(collection: Collection<Int>): String = collection.joinToString(prefix = "[" , postfix = "]")

    println(toJSON(collection = linkedSetOf(1,2,3,4)))


    fun joinOptions(options: Collection<String>) = options.joinToString(separator = "," , prefix = "[" , postfix = "]")

    println(joinOptions(options = linkedSetOf("1","2","3","4")))


    fun foo(name: String, number: Int = 0, toUpperCase: Boolean = true) =
            (if (toUpperCase) name.toUpperCase() else name) + number

    fun useFoo() = listOf(
            foo("a"),
            foo("b", number = 1),
            foo("c", toUpperCase = true),
            foo(name = "d", number = 2, toUpperCase = true)
    )


    val month = "(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)"

    fun getPattern(): String {
        // 12 JAN 1889
        return """\d{2} $month \d{4}"""
    }

    val regex = getPattern().toRegex()

    println(regex.matches("12 JAN 1889"))
    println(regex.matches("12-34-1889"))


  val nullValue : String? = null

    fun checkNull(value: String?):Boolean{
        if(value == null){
            return true
        }
        return false
    }

    println("Null value : ${checkNull(nullValue)}")

    // interface Expr
    abstract class Expr
    class Num(val value: Int) : Expr()
    class Sum(val left: Expr, val right: Expr) : Expr()

    fun eval(expr: Expr): Int =
            when (expr) {
                is Num -> expr.value
                is Sum -> (eval(expr.left)+eval(expr.right))
                else -> throw IllegalArgumentException("Unknown expression")
            }

    eval(Num(5))
    eval(Sum(Num(4) , Num(5)))

    val sum = eval(Sum(Sum(Sum(Num(4) , Sum(Num(4) , Num(5))) , Num(5)) , Sum(Num(4) , Num(5))))
    println(sum)

    data class RationalNumber(val numerator: Int, val denominator: Int)

    fun Int.r():RationalNumber = RationalNumber(this , 1)
    fun Pair<Int , Int>.r():RationalNumber = RationalNumber(first , second)

    Collections.sort(arrayListOf(100,20,3,0), object: Comparator<Int> {
        override fun compare(o1: Int, o2: Int): Int {
            return o2-o1
        }

    })





}


fun chapter1_example1() {
    class BlueberryCupCake {
        val flavour = "Blueberry"
    }

    val blueberryCupCake = BlueberryCupCake()
    println("BlueberryCupCake flavour : ${blueberryCupCake.flavour}")

    // another declaration for class
    class AlmondCupCake(val flavour: String = "Almond")

    val almondCupCake = AlmondCupCake()
    println("AlmondCupCake flavour : ${almondCupCake.flavour}")

    // create Cupcake class parent for both almondCupCake and blueberryCupCake classes
    //Now, we can define several instances with different flavors
    class Cupcake(val flavour: String)

    val type1CupCake = Cupcake("type1")
    println("typ1CupCake flavour : ${type1CupCake.flavour}")
    val type2CupCake = Cupcake("type2")
    println("typ2CupCake flavour : ${type2CupCake.flavour}")
    val type3CupCake = Cupcake("type3")
    println("typ3CupCake flavour : ${type3CupCake.flavour}")

    // methods
    class Cupcake2(val flavour: String) {
        fun eat(): String {
            return "mom,mom,mom... delicious $flavour cupcake"
        }

        fun anotherSyntax() = "'anotherSyntax' mom,mom,mom... delicious $flavour cupcake"
    }

    val almond = Cupcake2("almond")
    println(almond.eat())
    println(almond.anotherSyntax())

}

fun chapter1_example2() {
    class Biscuit(val flavour: String) {
        fun eat() = "mom,mom,mom... delicious $flavour biscuit"
    }

    class Cupcake(val flavour: String) {
        fun eat(): String {
            return "mom,mom,mom... delicious $flavour cupcake"
        }
    }

    // two classes are the same We could do refactor these classes to reduce code duplication
    // refactor
    open class BakeryGood(val flavour: String) {
        fun eat() = "mom,mom,mom... delicious $flavour ${name()}"

        open fun name() = "BakeryGood"
    }

    //in kotlin you can't extend a class that isn't open.

    class RefactorCupcakeClass(flavour: String) : BakeryGood(flavour) {
        override fun name() = "Cupcake"
    }

    class RefactorBiscuitClass(flavour: String) : BakeryGood(flavour) {
        override fun name() = "Biscuit"
    }

    val blueberry: BakeryGood = RefactorCupcakeClass("blueberry")
    println(blueberry.eat())
    val almond: BakeryGood = RefactorBiscuitClass("almond")
    println(almond.eat())

    // subclasses can be extended
    open class A(val message: String) {
        open fun printMessage() = "Message : $message From ${name()}"
        open fun name() = "A"
    }

    open class B(message: String) : A(message) {
        override fun name(): String {
            return "B"
        }
    }

    class C(val message1: String, val message2: String) : B(message1) {
        override fun name(): String {
            return "C"
        }

        override fun printMessage(): String {
            return "Message1 : $message1 , Message2 : $message2 From ${name()}"
        }
    }

    println(C("M1", "M2").printMessage())

}

fun chapter1_example3() {
    abstract class BakeryGood(val flavour: String) {
        fun eat() = "mom,mom,mom... delicious $flavour ${name()}"

        abstract fun name(): String
    }

    class Donut(flavour: String, val topping: String) : BakeryGood(flavour) {
        override fun name(): String {
            return "Donut with $topping"
        }

    }

    class Customer(val name: String) {
        fun eat(food: BakeryGood) {
            println("$name customer is eating... ${food.eat()}")
        }
    }

    val myDonut = Donut("Custard", "Powder Sugar");
    val customer = Customer("customer1")
    customer.eat(myDonut)

    // What happens if we want a simple BakeryGood

    customer.eat(object : BakeryGood("Cupcake") {
        override fun name(): String {
            return "almond"
        }
    })

    val food: BakeryGood = object : BakeryGood("Sada") {
        override fun name(): String {
            return "Biscuit"
        }
    }
    customer.eat(food)

}

interface Bakeable {
    fun bake() = "is hot here ,isn't"
}

interface Fried {
    fun fry(): String
}

fun chapter1_example4() {
    abstract class BakeryGood(val flavour: String) {
        fun eat() = "mom,mom,mom... delicious $flavour ${name()}"

        abstract fun name(): String
    }

    class Cupcake(flavor: String) : BakeryGood(flavor), Bakeable {
        override fun name(): String {
            return "Cupcake"
        }
    }

    val almond = Cupcake("almond")
    println(almond.eat())
    println(almond.bake())


    // extends multiple interfaces and just one class
    class Donut(flavor: String, val topping: String) :
            Bakeable, Fried,
            BakeryGood(flavor) {
        override fun fry(): String {
            return "Swimming on Oil"
        }

        override fun name(): String {
            return "Donut With $topping"
        }
    }

    val myDonut = Donut("", "powder Sugar")
    println("${myDonut.eat()}\n${myDonut.bake()}\n${myDonut.fry()}")

    // As with abstract classes, object expressions can be used with interfaces

    val fry = object : Fried {
        override fun fry(): String {
            return "from object expression"
        }
    }

    println(fry.fry())

}

class Outer {
    val exp = object {
        val prop = "propValue"
        fun function(): String {
            return "expFunction"
        }
    }

}

object Oven {
    fun process(product: Bakeable) {
        println(product.bake())
    }
}

object BakeableObject : Bakeable {
    override fun bake(): String {
        return super.bake() + " From Bakeable Object"
    }
}


class PersonCompanion(val fname: String, val lname: String) {
    fun getName() = "$fname $lname"

    // companion object can has a name or not

    companion object Factory{
        fun person1(): PersonCompanion {
            return PersonCompanion("ahmed", "baroty")
        }

        fun person2(): PersonCompanion {
            return PersonCompanion("mo", "salah")
        }
    }









}


fun kotlinTutorial() {

    var age = 12 //mutable
    val name = "ahmedbaroty" // immutable

    println("name : $name , age : $age")
    // variable
    val maxInt: Int = Int.MAX_VALUE
    val minInt: Int = Int.MIN_VALUE

    println("maxInt : $maxInt , minInt : $minInt")

    val maxDouble: Double = Double.MAX_VALUE
    val minDouble: Double = Double.MIN_VALUE

    println("maxDouble : $maxDouble , minDouble : $minDouble")

    val maxFloat: Float = Float.MAX_VALUE
    val minFloat: Float = Float.MIN_VALUE

    println("maxFloat : $maxFloat , minFloat : $minFloat")

    val maxShort: Short = Short.MAX_VALUE
    val minShort: Short = Short.MIN_VALUE

    println("maxShort : $maxShort , minShort : $minShort")

    val maxByte: Byte = Byte.MAX_VALUE
    val minByte: Byte = Byte.MIN_VALUE

    println("maxByte : $maxByte , minByte : $minByte")


    val char: Char = 'A'
    println("char $char is Char ${char is Char}")

    val bool: Boolean = true
    println("bool $char is Boolean ${bool is Boolean}")

    // casting

    println("cast char $char to int : ${char.toInt()}")
    println("cast int 65 to char : ${65.toChar()}")
    println("cast float 3.14  to int : ${3.14.toInt()}")

    //strings

    val username = "ahmedbaroty"
    val discription = """ this is a
        -----------------------------
        long string"""
    val fName: String = "ahmed"
    val lName: String = "mohammed"

    var fullName = "$fName $lName"

    println("""username : $username , length : ${username.length}
fullname : $fullName , length : ${fullName.length}
description : $discription , length : ${discription.length} """)

    var str1 = "A random string1"
    var str2 = "a random String2"

    println("str1 : $str1  , str2 : $str2")
    println("2nd Index : ${str1[2]}")
    println("str1 Index from 2 to 7 : ${str1.subSequence(2, 8)}")
    println("str1 is equal to str2 : ${str1 == str2}")
    println("str1 compare with str2 : ${str1.compareTo(str2)}")
    println("str1 contains random : ${str1.contains("random")}")
    println("str1 to uppercase : ${str1.toUpperCase()}")

    // Arrays
    var array = arrayOf(12, 345, 43.909, 44.01f, "GOGO", 'A')
    println("array length : ${array.size}")
    println("2nd element in array : ${array[1]}")
    println("array first : ${array.first()}")
    println("array last : ${array.last()}")
    println("array contains true : ${array.contains(true)}")
    println("array index of true : ${array.indexOf(true)}")

    var partArray = array.copyOfRange(0, 3)
    println("Part Array 2nd value : ${partArray[1]}")
    var sequenceArray = Array(5) { x -> x + x }
    print("sequenceArray Data : [ ")
    for (x in sequenceArray) {
        print("$x ")
    }
    println("]")

    var specificArrayType: Array<Int> = arrayOf(1, 2, 3)

    //Ranges
    val oneTo10 = 1..10
    val alpha = "A".."Z"

    println("R is Alpha : ${alpha.contains("R")}")
    println("R is Alpha : ${"R" in alpha}")

    val tenTo1 = 10.downTo(1)
    val tenTo1Reversed = oneTo10.reversed();

    println("ten to 1 equal to tenTo 1 reversed : ${tenTo1 == tenTo1Reversed}")

    val twoTo20 = 2.rangeTo(20)

    val rng3 = oneTo10.step(3)

    for (x in rng3) println("rng 3 : $x")

    // conditional

    val grade = 8
    if (grade < 5) {
        println("Grade is : D")
    } else if (grade >= 5 && grade <= 6) {
        println("Grade is : C")
    } else if (grade >= 7 && grade <= 8) {
        println("Grade is : B")
    } else {
        println("Grade is : A")
    }

    when (grade) {
        in 0..4 -> println("Grade is : D")
        5, 6 -> println("Grade is : C")
        7, 8 -> println("Grade is : B")
        else -> println("Grade is : A")
    }


    // looping

    for (x in 1..10) println("loop : $x")

    val rand = Random();
    val magicNumber = rand.nextInt(50) + 1

    var guess = 0

    while (magicNumber != guess) {
        guess += 1
    }

    println("Magic Number was $guess")

    for (x in 1..20) {
        if (x % 2 == 0) continue
        println("odd value : $x")
        if (x == 15) break
    }

    var array3: Array<Int> = arrayOf(1, 7, 89, 90)

    for (i in array3.indices)
        println("array 3 : ${array3[i]}")

    for ((index, value) in array3.withIndex())
        println("index : $index , value : $value")


    //functions

    fun add(x: Int, y: Int): Int = x + y
    println("5 + 9 = ${add(5, 9)}")

    fun subtract(x: Int, y: Int) = x - y
    println("5 - 9 = ${subtract(5, 9)}")

    // naming parameters
    println("9 - 5 = ${subtract(y = 5, x = 9)}")

    fun sayHello(name: String = "World") = println("Hello $name !!!")
    sayHello()
    sayHello(name = "Ahmed")

    fun nextTwo(num: Int): Pair<Int, Int> = Pair(num + 1, num + 2)
    val (two, three) = nextTwo(93)
    println("next two values from one 93 : (two : $two , three : $three )")

    fun getSum(vararg nums: Int): Int {
        var sum = 0;
        nums.forEach { n -> sum += n }
        return sum;
    }

    println("Summation get Sum : ${getSum(1, 2, 3, 4, 5, 6, 7, 8, 9)}")

    val multiply = { num1: Int, num2: Int -> num1 * num2 }
    println("multiply 3 , 4 = ${multiply(3, 4)}")


    // calc factorial
    fun fact(x: Int): Int {
        fun factTail(y: Int, z: Int): Int {
            if (y == 1) return z
            else return factTail(y - 1, z * y)
        }
        return factTail(x, 1)
    }

    println("Factorial 5 = ${fact(5)}")

    val numList = 1..20

    val evenList = numList.filter { it % 2 == 0 }
    evenList.forEach { n -> println("Even Number : $n") }

    fun makeMathFunc(num1: Int): (Int) -> Int = { num2 -> num1 * num2 }
    println("multiply 5 * 5 : ${makeMathFunc(5)(5)}")

    fun mathOnArray(numList: Array<Int>, customFunc: (num: Int) -> Int) {
        for (num in numList) {
            println("math On Array : ${customFunc(num)}")
        }
    }
    mathOnArray(arrayOf(1, 2, 3, 4, 5, 6)) { num: Int -> num * 2 }

    // collection operation
    val numList2 = 1..20
    val listSum = numList2.reduce { a, b -> a + b }
    println("list sum reduce : $listSum")
    val listSum2 = numList2.fold(5) { a, b -> a + b }
    println("list sum fold : $listSum2")
    println("list even nums by filter : ${numList2.filter { it % 2 == 0 }}")
    println("list has even nums by any: ${numList2.any { it % 2 == 0 }}")
    println("list values all even nums by all: ${numList2.all { it % 2 == 0 }}")


    val times7 = numList2.map { it * 7 }
    times7.forEach { n -> println("list times by 7 using map: $n") }


    // exception handle

    val divisor = 0

    try {
        if (divisor == 0) {
            throw IllegalArgumentException("Can't divide by zero")
        } else {
            println(" 5 divided by ${5 / divisor}")
        }
    } catch (e: IllegalArgumentException) {
        println("${e.message}")
    }

    // mutable List

    var list1: MutableList<Int> = mutableListOf(1, 2, 3, 4, 5, 6, 7, 89, 12)
    var list2: List<Int> = listOf(1, 2, 3)

    list1.add(100_000)
    println("1st : ${list1.first()}")
    println("last : ${list1.last()}")
    println("2nd : ${list1[1]}")

    var list3 = list1.subList(0, 4)
    println("list 3 length : ${list3.size}")

    println("Mutable List $list3")
    list3.remove(element = 1)
    println("Mutable List remove element : $list3")
    list3.removeAt(index = 0)
    println("Mutable List remove item at index : $list3")
    list3.add(1234)
    println("Mutable List add item : $list3")
    list3.clear()
    println("Mutable List clear : $list3")

    // maps

    val map = mutableMapOf<Int, Any?>()
    val map2 = mutableMapOf(1 to "ahmed", 2 to "baroty")

    map[1] = "Fatima"
    map[2] = 43
    map[3] = "zizo"

    map.remove(3);

    for ((key, value) in map) {
        println("Map key $key and value $value")
    }

    // null value
    var nullVal: String? = null
    fun myFun(): String? = null

    // classes
    open class Animal(var name: String, var height: Double, var weight: Double) {
        init {
            val regex = Regex(".*\\d+.*")
            require(!name.matches(regex)) { "Animal name can't contain numbers" }

            require(height > 0) {
                "Height must be Greater than 0"
            }

            require(weight > 0) {
                "weight must be Greater than 0"
            }
        }

        open fun getInfo() {
            println("name is ${this.name} and height is ${this.height} and weight is ${this.weight}")
        }
    }


    class Dog(name: String,
              height: Double,
              weight: Double,
              var owner: String) : Animal(name, height, weight) {

        override fun getInfo() {
            println("name is ${this.name} and height is ${this.height} and weight is ${this.weight} and owner is ${this.owner}")
        }
    }

    try {
        val cat: Animal = Animal("Tom", 12.34, 111.0)
        cat.getInfo()

        val spot = Dog("spot", 12.34, 111.0, "baroty")
        spot.getInfo()
    } catch (e: Exception) {
        println(e.message)
    }

    Bird("bird1", true).fly(234.09)
    Bird("bird2", false).fly(234.09)
}

// interfaces
interface Flyable {
    var flies: Boolean
    fun fly(distMile: Double)
}

class Bird constructor(val name: String, override var flies: Boolean = true) : Flyable {
    override fun fly(distMile: Double) {
        if (flies) {
            println("$name flies $distMile miles")
        } else {
            println("$name can not fly")
        }
    }


}


// --------------------------------------------------
// - Functions
// --------------------------------------------------


fun functions() {
    println("Hello World !!!")

    printMessage("Hello Kotlin")

    printMessageWithPrefix("Here We Learn Kotlin", "Log")

    printMessageWithPrefix("Here We Learn Kotlin")

    printMessageWithPrefix(prefix = "Log", message = "Here We Learn Kotlin")

    println(sum(33, 24))

    println(multiply(33, 24))

    // repeat
    infix fun Int.times(str: String) = str.repeat(this)
    println(2 times "Bye")

    // pair
    println("ahmed" to "baroty")


}

fun printMessage(message: String) {
    println(message)
}

fun printMessageWithPrefix(message: String, prefix: String = "Info") {
    println("[$prefix] $message")
}

fun sum(x: Int, y: Int): Int {
    return x + y
}

fun multiply(x: Int, y: Int) = x * y


// --------------------------------------------------
// - Basic Types
// --------------------------------------------------

fun numbers() {
    val a: Int? = 10000
    val d: Double? = 100.00
    val f: Float? = 100.00f
    val l: Long? = 1000000004
    val s: Short? = 10
    val b: Byte? = 1

    val creditCardNumber: Long? = 1234_9873_3456_1232L

    println("Your Int Value is $a")
    println("Your Double  Value is $d")
    println("Your Float Value is $f")
    println("Your Long Value is $l")
    println("Your Short Value is $s")
    println("Your Byte Value is $b")
    println("underscore number $creditCardNumber")

    val byteToInt: Int = b!!.toInt()
    println("byteToInt $byteToInt")

}

fun characters() {
    val letter: Char = 'A'
    println("character Type")
    println("character letter is  : $letter")
}

fun booleans() {
    val bool: Boolean? = true
    println("Boolean Type")
    println("Boolean bool value : $bool")

}

fun strings() {

}