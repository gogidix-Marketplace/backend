package com.gogidix.aiservices.leadgenerationaiservice.domain.model;
import org.junit.jupiter.api.*; import static org.assertj.core.api.Assertions.*;
class ContactInfoTest {
    @Test void create() { var c = ContactInfo.builder().email("t@t.com").firstName("J").lastName("D").company("C").jobTitle("CEO").build(); assertThat(c.getEmail()).isEqualTo("t@t.com"); assertThat(c.getFullName()).isEqualTo("J D"); assertThat(c.hasCompanyInfo()).isTrue(); }
    @Test void nullEmail() { assertThatThrownBy(() -> ContactInfo.builder().build()).isInstanceOf(Exception.class); }
    @Test void invalidEmail() { assertThatThrownBy(() -> ContactInfo.builder().email("bad").build()).isInstanceOf(Exception.class); }
    @Test void fullNameFirstOnly() { assertThat(ContactInfo.builder().email("t@t.com").firstName("J").build().getFullName()).isEqualTo("J"); }
    @Test void fullNameLastOnly() { assertThat(ContactInfo.builder().email("t@t.com").lastName("D").build().getFullName()).isEqualTo("D"); }
    @Test void fullNameNeither() { assertThat(ContactInfo.builder().email("t@t.com").build().getFullName()).isEqualTo("t@t.com"); }
    @Test void isDecisionMaker() { assertThat(ContactInfo.builder().email("t@t.com").jobTitle("CEO").build().isDecisionMaker()).isTrue(); }
    @Test void notDecisionMaker() { assertThat(ContactInfo.builder().email("t@t.com").jobTitle("Dev").build().isDecisionMaker()).isFalse(); }
    @Test void hasCompanyInfoTrue() { assertThat(ContactInfo.builder().email("t@t.com").company("C").build().hasCompanyInfo()).isTrue(); }
    @Test void hasCompanyInfoFalse() { assertThat(ContactInfo.builder().email("t@t.com").build().hasCompanyInfo()).isFalse(); }
    @Test void equality() { var c1 = ContactInfo.builder().email("t@t.com").build(); var c2 = ContactInfo.builder().email("t@t.com").build(); assertThat(c1).isEqualTo(c2); }
}
