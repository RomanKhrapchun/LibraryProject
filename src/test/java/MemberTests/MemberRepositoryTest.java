package MemberTests;

import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testSaveMember() {
        Member member = new Member();
        member.setName("John Smith");
        member.setMembershipDate(LocalDate.now());
        member.setBookBorrowed(false);

        Member savedMember = memberRepository.save(member);

        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getId()).isGreaterThan(0);
    }

    @Test
    void testFindMemberById() {
        Member member = new Member();
        member.setName("John Smith");
        member.setMembershipDate(LocalDate.now());
        member.setBookBorrowed(false);
        Member savedMember = memberRepository.save(member);
        Optional<Member> optionalMember = memberRepository.findById(savedMember.getId());
        assertThat(optionalMember).isPresent();
        assertThat(optionalMember.get().getName()).isEqualTo("John Smith");
    }

    @Test
    void testUpdateMember() {
        Long memberId = 12L;
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        Member member = optionalMember.get();
        member.setName("Jane Smith");
        memberRepository.save(member);

        Member updatedMember = memberRepository.findById(memberId).get();
        assertThat(updatedMember.getName()).isEqualTo("Jane Smith");
    }

    @Test
    void testDeleteMember() {
        Long memberId = 1L;
        memberRepository.deleteById(memberId);

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        assertThat(optionalMember).isNotPresent();
    }
}
