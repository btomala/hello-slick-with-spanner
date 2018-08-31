import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import SpannerProfile.api._
import slick.jdbc.meta._

class TablesSuite extends FunSuite with BeforeAndAfter with BeforeAndAfterEach with ScalaFutures {
  implicit override val patienceConfig = PatienceConfig(timeout = Span(600, Seconds))

  val suppliers = TableQuery[Suppliers]
  val coffees = TableQuery[Coffees]
  
  var db: Database = _

  def createSchema() = {
    db.run(DBIO.seq(Suppliers.create, Coffees.create)).futureValue
  }

  def insertSupplier(): Int =
    db.run(suppliers += (101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199")).futureValue
  
  before { db = Database.forConfig("spanner") }
  
  ignore("Creating the Schema works") {
    createSchema()
    
    val tables: Seq[MTable] = db.run(MTable.getTables).futureValue

    assert(tables.size == 2)
    assert(tables.count(_.name.name.equalsIgnoreCase("suppliers")) == 1)
    assert(tables.count(_.name.name.equalsIgnoreCase("coffees")) == 1)
  }

  test("Inserting a Supplier works") {

    val insertCount = insertSupplier()
    assert(insertCount == 1)
  }
  
  test("Query Suppliers works") {
    val results = db.run(suppliers.result).futureValue
    assert(results.size == 1)
    assert(results.head._1 == 101)
  }

  after {
    //don't know how to drop tables :/
    db.close
  }
}
