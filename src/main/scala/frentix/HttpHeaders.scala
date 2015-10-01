/**
 * <a href="http://www.openolat.org">
 * OpenOLAT - Online Learning and Training</a><br>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at the
 * <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache homepage</a>
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Initial code contributed and copyrighted by<br>
 * frentix GmbH, http://www.frentix.com
 * <p>
 */
package frentix

import java.nio.charset.StandardCharsets._

import com.fasterxml.jackson.databind.ObjectMapper
import io.gatling.http.response.{Response, StringResponseBody, ResponseWrapper}

import scala.collection.mutable

trait HttpHeaders {
  
	val headers = Map("""Accept""" -> """*/*""")

	val headers_json = Map("""Accept""" -> """application/json,text/html""")

	val headers_post = Map(
		"""Content-Type""" -> """application/x-www-form-urlencoded""",
		"""Origin""" -> """http://localhost""")

	val extractJsonResponse : PartialFunction[io.gatling.http.response.Response,io.gatling.http.response.Response] = {
		case response : Response if response.isReceived =>
			new ResponseWrapper(response) {
				val extractedResponse = new mutable.StringBuilder
				extractedResponse.append("<!DOCTYPE html><html><head><title>Fragment</title></head><body>")
				val jsonResponse = (new ObjectMapper).readTree(response.body.string)
				val cmds = jsonResponse.get("cmds");
				0 to cmds.size() - 1 foreach { i => {
					val cmd = cmds.get(i)
					if(cmd.get("cmd").asInt() == 2) {
						val cps = cmd.get("cda").get("cps");
						0 to cps.size() - 1 foreach { j => {
							val cp = cps.get(j);
							if (cp != null && cp.has("hfrag")) {
								val htmlFragment = cp.get("hfrag").asText()
								if (!htmlFragment.equals("<!-- empty -->")) {
									extractedResponse.append(htmlFragment)
								}
							}
						}}
					}
				}}
				extractedResponse.append("</body></html>")
				//println("***********************************************************")
				//println(extractedResponse.toString())
				//println("***********************************************************")
				override def body = StringResponseBody(extractedResponse.toString(), UTF_8)
			}
	}

}