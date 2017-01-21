package lila.practice

import lila.study.{ Study, Chapter }

case class PracticeStructure(
    sections: List[PracticeSection]) {

  def study(id: Study.Id): Option[PracticeStudy] =
    sections.flatMap(_ study id).headOption
}

case class PracticeSection(
    id: String,
    name: String,
    studies: List[PracticeStudy]) {

  def study(id: Study.Id): Option[PracticeStudy] = studies find (_.id == id)
}

case class PracticeStudy(
    id: Study.Id, // study ID
    name: String,
    desc: String,
    chapters: List[Chapter.IdName]) {

  def chapterIds = chapters.map(_.id)
}

object PracticeStructure {

  def make(conf: PracticeConfig, chapters: Map[Study.Id, Vector[Chapter.IdName]]) =
    PracticeStructure(
      sections = conf.sections.map { sec =>
        PracticeSection(
          id = sec.id,
          name = sec.name,
          studies = sec.studies.map { stu =>
            val id = Study.Id(stu.id)
            PracticeStudy(
              id = id,
              name = stu.name,
              desc = stu.desc,
              chapters = chapters.get(id).??(_.toList))
          })
      })
}
