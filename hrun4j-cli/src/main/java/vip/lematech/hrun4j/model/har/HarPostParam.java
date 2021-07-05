/**
 * har - HAR file reader, writer and viewer
 * Copyright (c) 2014-2016, Sandeep Gupta
 * <p>
 * http://sangupta.com/projects/har
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vip.lematech.hrun4j.model.har;


import lombok.Data;

/**
 * @author sangupta/lematech@foxmail.com
 * website http://lematech.vip/
 * @version 1.0.1
 */

@Data
public class HarPostParam extends HarBaseModel {

	private String fileName;

	private String contentType;

	@Override
	public String toString() {
		return "[Post Param: " + this.name + "=" + this.value + "]";
	}

	@Override
	public int hashCode() {
		if (this.name == null) {
			return -1;
		}

		return this.name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof HarPostParam)) {
			return false;
		}

		if (this.name == null) {
			return false;
		}

		HarPostParam harPostParam = (HarPostParam) obj;
		return this.name.equals(harPostParam.name);
	}

}
