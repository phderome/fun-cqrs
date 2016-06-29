package lottery.domain.service

import com.typesafe.scalalogging.LazyLogging

import scala.util.Try

trait InMemoryRepository {

  type Model
  type Identifier

  private var store: Map[Identifier, Model] = Map()

  def find(id: Identifier): Try[Model] = Try(store(id))

  def save(model: Model): Unit = {
    store = store + ($id(model) -> model)
  }

  def deleteById(id: Identifier): Unit =
    store = store.filterKeys(_ != id)

  def updateById(id: Identifier)(updateFunc: (Model) => Model): Try[Model] =
    find(id).map { model =>
      val updated = updateFunc(model)
      save(updated)
      updated
    }

  def fetchAll: Seq[Model] =
    store.values.toSeq

  /** Extract id van Model */
  protected def $id(model: Model): Identifier

}
