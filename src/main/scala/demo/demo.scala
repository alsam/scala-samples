package demo

trait BufReader[T] {
  var buf: Array[Byte] = Array[Byte]()

  var bp: Int = 0

    def set_buffer(content: Array[Byte]): Unit = {
      buf = content
      bp = 0
    }

  def next: T

  def get(bp: Int): T
}

object BufReader {

  var content = Array[Byte]()

  implicit object IntBufReader extends BufReader[Int] {

    def next: Int =  {
      bp += 4
      get(bp - 4)
    }

    def get(bp: Int): Int = 
      ((buf(bp    ) & 0xff) << 24) +
      ((buf(bp + 1) & 0xff) << 16) +
      ((buf(bp + 2) & 0xff) <<  8) +
       (buf(bp + 3) & 0xff)

  }

}

class BufferReader(content: Array[Byte]) {
  import java.io._

  /** the buffer containing the file
   */
  val buf: Array[Byte] = content

  /** the current input pointer
   */
  var bp: Int = 0

  /** read an integer
   */
  def nextInt: Int = {
    bp += 4
    getInt(bp - 4)
  }

  def next[T](implicit e: T =:= Int): Int = {
    bp += 4
    getInt(bp - 4) // .asInstanceOf[T]
  }

  /** read a float
   */
  def nextFloat: Float = java.lang.Float.intBitsToFloat(nextInt)

  def next[T](implicit e: T =:= Float): Float = {
    bp += 4
    getFloat(bp - 4) // .asInstanceOf[T]
  }

  /** extract an integer at position bp from buf
   */
  def getInt(bp: Int): Int =
        ((buf(bp    ) & 0xff)      ) +
        ((buf(bp + 1) & 0xff) <<  8) +
        ((buf(bp + 2) & 0xff) << 16) +
        ((buf(bp + 3) & 0xff) << 24)

  /** extract a float at position bp from buf
   */
  def getFloat(bp: Int): Float = java.lang.Float.intBitsToFloat(getInt(bp))

}

object BufferReader {
  def apply(content: Array[Byte]) = new BufferReader(content)
}


/////////////////////////////////////////////////////////////////////
class Vector3[T](var x:T, var y:T, var z:T)(implicit ev: Numeric[T]) {

  import ev._

  def this()(implicit n: Numeric[T])         = this(n.zero, n.zero, n.zero)
  def this(x:T)(implicit n: Numeric[T])      = this(x,      n.zero, n.zero)
  def this(x:T, y:T)(implicit n: Numeric[T]) = this(x,      y,      n.zero)

  override def toString = "(" + x + "," + y + "," + z + ")"

  def read(buf: Array[Byte])(implicit i:BufReader[T]): Unit = {
    i.set_buffer(buf)
    x = i.next
    y = i.next
    z = i.next
    println("x:" + x + " y:" + y + " z:" + z)
  }

  def scal(s:T) = new Vector3[T](s*x, s*y, s*z)

  def vol = x*y*z

  def is_zero = (x == ev.zero && y == ev.zero && z == ev.zero)

  def toTuple = (x, y, z)

  def +(other: Vector3[T]) = new Vector3[T](x+other.x, y+other.y, z+other.z)
  def -(other: Vector3[T]) = new Vector3[T](x-other.x, y-other.y, z-other.z)
  def *(other: Vector3[T]) = new Vector3[T](x*other.x, y*other.y, z*other.z)
  def sdot(other: Vector3[T]):T = x*other.x + y*other.y + z*other.z
}

object Vector3 {
  def apply[T: Numeric]()              = new Vector3[T]()
  def apply[T: Numeric](x:T)           = new Vector3[T](x)
  def apply[T: Numeric](x:T, y:T)      = new Vector3[T](x,y)
  def apply[T: Numeric](x:T, y:T, z:T) = new Vector3[T](x,y,z)
}

class Box3[T](var lo: Vector3[T], var hi: Vector3[T])(implicit ev: Numeric[T]) {
  def this()(implicit n: Numeric[T]) = this(new Vector3(), new Vector3())
  override def toString = "(" + lo + "," + hi + ")"
}

object Box3 {
  def apply[T: Numeric](lo:Vector3[T], hi:Vector3[T]) = new Box3[T](lo,hi)
  def apply[T: Numeric]() = new Box3[T]()
}

class DumpHeader { //extends scala.Serializable {
  var DumpShape              : Vector3[Int]  = Vector3()
  var CacheTextureExtent     : Box3[Float]   = Box3()
  var WorldTextureExtent     : Box3[Float]   = Box3()
  var CacheTextureResolution : Int           = 0
  var WorldTextureResolution : Int           = 0
  var CacheMipLevels         : Int           = 0
  var Padding1               : Int           = 0
  var Padding2               : Int           = 0

  override def toString = "DumpHeader:" + "\n\tDumpShape: " + DumpShape + "\n\tCacheMipLevels" + CacheMipLevels
}

// scala> :paste
// // Entering paste mode (ctrl-D to finish)
// [...]
// // Exiting paste mode, now interpreting.
// 
// defined class Vector3
// defined module Vector3
// defined class Box3
// defined module Box3
// defined class DumpHeader
// 
// scala> val a: Vector3[Float] = Vector3()
// a: Vector3[Float] = (0.0,0.0,0.0)
// 
// scala> val b: Box3[Float] = Box3()
// b: Box3[Float] = ((0.0,0.0,0.0),(0.0,0.0,0.0))
// 
// scala> val a: Vector3[Float] = Vector3(1,2,3)
// a: Vector3[Float] = (1.0,2.0,3.0)
// 
// scala> val b: Vector3[Float] = Vector3(4,5,6)
// b: Vector3[Float] = (4.0,5.0,6.0)
// 
// scala> a*b
// res0: Vector3[Float] = (4.0,10.0,18.0)
// 

//object Main extends App {

object Test {

  def vec3_demo {
    //val tri = Array[Byte](0,0,0,0x40,  0,0,0x5,0,  0,0,0x2,(-(0xFF-0xD0)-1).toByte)
    val tri = Array[Byte](0,0,0,0x40,  0,0,0x5,0,  0,0,0x2,0xD0.toByte)

    val a: Vector3[Int] = Vector3()

    a.read(tri)

    println("a : " + a)
  }
}

