import javax.inject.Inject

import play.api.http.HttpFilters

class Filters @Inject() (corsFilter: CORSFilter) extends HttpFilters {
  def filters = Seq(corsFilter)
}