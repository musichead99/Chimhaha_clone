package net.chimhaha.clone.domain.menu;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @AfterEach
    public void cleanup(){
        menuRepository.deleteAll();
    }

    @Test
    public void 메뉴_등록() {
        // given
        Menu menu = Menu.builder()
                .name("침착맨")
                .build();

        // when
        Menu storedMenu = menuRepository.save(menu);

        // then
        assertAll(
                () -> assertEquals("침착맨", storedMenu.getName()),
                () -> assertNotNull(storedMenu.getId())
        );
    }

    @Test
    public void 메뉴_전체_조회() {
        // given
        String[] list = {"침하하", "전체글", "침착맨", "웃음", "운동"};

        for(int i = 0; i < 5; i++) {
            menuRepository.save(Menu.builder()
                    .name(list[i])
                    .build());
        }

        // when
        List<Menu> menuList = menuRepository.findAll();

        // then
        assertAll(
                () -> assertEquals(5, menuList.size()),
                () -> assertEquals("침하하", menuList.get(0).getName()),
                () -> assertEquals("전체글", menuList.get(1).getName()),
                () -> assertEquals("침착맨", menuList.get(2).getName()),
                () -> assertEquals("웃음", menuList.get(3).getName()),
                () -> assertEquals("운동", menuList.get(4).getName())
        );
    }

    @Test
    public void 메뉴_수정() {
        // given
        Menu menu = Menu.builder()
                .name("침하하")
                .build();

        menuRepository.save(menu);

        // when
        menu.update("웃음");
        Menu updatedMenu = menuRepository.save(menu);

        // then
        assertAll(
                () -> assertEquals("웃음", updatedMenu.getName()),
                () -> assertEquals(menu.getId(), updatedMenu.getId())
        );
    }

    @Test
    public void 메뉴_삭제() {
        // given
        Menu menu = Menu.builder()
                .name("침하하")
                .build();

        menuRepository.save(menu);

        // when
        menuRepository.deleteById(menu.getId());
        Optional<Menu> optionalMenu = menuRepository.findById(menu.getId());

        // then
        assertAll(
                () -> assertFalse(optionalMenu.isPresent())
        );
    }
}
