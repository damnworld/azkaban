/*
 * Copyright 2020 LinkedIn Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package azkaban.version;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class VersionSetBuilderTest {
    @Test
    public void testVersionSet() throws IOException {
        // Test if the elements are ordered by keys, not by order of addition
        LocalVersionSetLoader localVersionSetLoader = new LocalVersionSetLoader();
        VersionSet versionSet = new VersionSetBuilder(localVersionSetLoader)
                .addElement("key1", "value1")
                .addElement("key3", "value3")
                .addElement("key2", "value2")
                .build();
        Assert.assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"}", versionSet.getVersionSetJsonString());
        Assert.assertEquals("43966138aebfdc4438520cc5cd2aefa8", versionSet.getVersionSetMd5Hex());
        Assert.assertEquals(1, versionSet.getVersionSetNum());

        // Test if the version set num remains the same
        VersionSet versionSet2 = new VersionSetBuilder(localVersionSetLoader)
                .addElement("key1", "value1")
                .addElement("key3", "value3")
                .addElement("key2", "value2")
                .build();
        Assert.assertEquals(1, versionSet2.getVersionSetNum());

        // Test that post removal, version set num is different
        localVersionSetLoader.removeVersionSet("43966138aebfdc4438520cc5cd2aefa8");
        VersionSet versionSet3 = new VersionSetBuilder(localVersionSetLoader)
                .addElement("key1", "value1")
                .addElement("key3", "value3")
                .addElement("key2", "value2")
                .build();
        Assert.assertEquals(2, versionSet3.getVersionSetNum());
    }
}
