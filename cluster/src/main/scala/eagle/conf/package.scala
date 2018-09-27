package eagle

package object conf {

  case class EagleConfig(http: HttpConfig)
  case class HttpConfig(bindPort: Int, bindInterface: String)
}
