package org.nting.toolkit.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Lists;

public class GwtCompatibleUtilsTest {

    @Test
    public void testReplaceAll() {
        assertEquals("{}a{}b{}", GwtCompatibleUtils.replaceAll(" a b ", ' ', "{}"));
        assertEquals("{}{}{}", GwtCompatibleUtils.replaceAll("   ", ' ', "{}"));
    }

    @Test
    public void testAbbreviateFileName() {
        assertEquals("qwert.txt", GwtCompatibleUtils.abbreviateFileName("qwert.txt", 10));
        assertEquals("qwertz.txt", GwtCompatibleUtils.abbreviateFileName("qwertz.txt", 10));
        assertEquals("qwertz.txt", GwtCompatibleUtils.abbreviateFileName("qwertz0.txt", 10));
        assertEquals("qwertz.txt", GwtCompatibleUtils.abbreviateFileName("qwertz00.txt", 10));
        assertEquals("qwertz.txt", GwtCompatibleUtils.abbreviateFileName("qwertz00000.txt", 10));

        assertEquals("qwertztxt", GwtCompatibleUtils.abbreviateFileName("qwertztxt", 10));
        assertEquals("qwertzutxt", GwtCompatibleUtils.abbreviateFileName("qwertzutxt", 10));
        assertEquals("qwertzutxt", GwtCompatibleUtils.abbreviateFileName("qwertzutxt0", 10));
        assertEquals("qwertzutxt", GwtCompatibleUtils.abbreviateFileName("qwertzutxt00", 10));
        assertEquals("qwertzutxt", GwtCompatibleUtils.abbreviateFileName("qwertzutxt00000", 10));

        assertEquals("0.glossary", GwtCompatibleUtils.abbreviateFileName("0123456789.glossary", 10));
        assertEquals("012345678", GwtCompatibleUtils.abbreviateFileName("0123456789.glossary", 9));
        assertEquals("01234567", GwtCompatibleUtils.abbreviateFileName("0123456789.glossary", 8));
    }

    @Test
    public void testCountMatches() {
        assertEquals(0, GwtCompatibleUtils.countMatches("", "xx"));
        assertEquals(1, GwtCompatibleUtils.countMatches("xx", "xx"));
        assertEquals(1, GwtCompatibleUtils.countMatches("axx", "xx"));
        assertEquals(1, GwtCompatibleUtils.countMatches("axxa", "xx"));
        assertEquals(1, GwtCompatibleUtils.countMatches("xxa", "xx"));
        assertEquals(0, GwtCompatibleUtils.countMatches("xaxa", "xx"));
        assertEquals(3, GwtCompatibleUtils.countMatches("xxxxxx", "xx"));
        assertEquals(3, GwtCompatibleUtils.countMatches("axxaxxaxxa", "xx"));
        assertEquals(1, GwtCompatibleUtils.countMatches("xxx", "xx"));
    }

    @Test
    public void testFindMatches() {
        assertEquals(Lists.newArrayList(), GwtCompatibleUtils.findMatches("", "xx"));
        assertEquals(Lists.newArrayList(0), GwtCompatibleUtils.findMatches("xx", "xx"));
        assertEquals(Lists.newArrayList(1), GwtCompatibleUtils.findMatches("axx", "xx"));
        assertEquals(Lists.newArrayList(1), GwtCompatibleUtils.findMatches("axxa", "xx"));
        assertEquals(Lists.newArrayList(0), GwtCompatibleUtils.findMatches("xxa", "xx"));
        assertEquals(Lists.newArrayList(), GwtCompatibleUtils.findMatches("xaxa", "xx"));
        assertEquals(Lists.newArrayList(0, 2, 4), GwtCompatibleUtils.findMatches("xxxxxx", "xx"));
        assertEquals(Lists.newArrayList(1, 4, 7), GwtCompatibleUtils.findMatches("axxaxxaxxa", "xx"));
        assertEquals(Lists.newArrayList(0), GwtCompatibleUtils.findMatches("xxx", "xx"));
    }
}