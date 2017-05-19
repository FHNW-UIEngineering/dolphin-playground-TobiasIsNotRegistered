package myapp.util.veneer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dieter Holz
 */
public class FX_EnumAttributeTest {
    @Test
    public void testCreateRegex(){
        //given

        //when
        String regex = EnumAttributeFX.createRegex(SomeEnum.class);

        //then
        assertEquals("((?i)FIRST){1}|((?i)SECOND){1}", regex);
        assertTrue("FIRST".matches(regex));
        assertTrue("first".matches(regex));
        assertTrue("SECOND".matches(regex));
        assertTrue("second".matches(regex));
        assertFalse("F".matches(regex));
    }

    private enum SomeEnum{
        FIRST, SECOND
    }

}