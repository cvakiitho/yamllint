/**
 * Copyright (c) 2018, Sylvain Baudoin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yaml.yamllint;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.yaml.yamllint.rules.RuleTester.getFakeConfig;

public class LinterTest extends TestCase {
    public void testRunOnString() throws IOException, YamlLintConfigException {
        Linter.run("test: document", getFakeConfig());
    }

    public void testRunOnNonAsciiChars() throws IOException, YamlLintConfigException {
        String s = "- hétérogénéité\n" +
               "# 19.99\n";
        Linter.run(s, getFakeConfig());
        Linter.run(new String(s.getBytes("UTF8"), Charset.forName("utf-8")), getFakeConfig());
        Linter.run(new String(s.getBytes("UTF8"), Charset.forName("iso-8859-15")), getFakeConfig());

        s = "- お早う御座います。\n" +
                "# الأَبْجَدِيَّة العَرَبِيَّة\n";
        Linter.run(s, getFakeConfig());
        Linter.run(new String(s.getBytes("UTF-8"), "UTF-8"), getFakeConfig());
    }

    public void testRunWithIgnore() throws IOException, YamlLintConfigException {
        YamlLintConfig conf = new YamlLintConfig("rules:\n" +
                "  indentation:\n" +
                "    spaces: 2\n" +
                "    indent-sequences: true\n" +
                "    check-multi-line-strings: false\n" +
                "ignore: |\n" +
                "  .*\\.txt$\n" +
                "  foo.bar\n");
        assertEquals(0, Linter.run(conf, new File("/my/file.txt")).size());
        assertEquals(0, Linter.run(conf, new File("foo.bar")).size());
    }
}
