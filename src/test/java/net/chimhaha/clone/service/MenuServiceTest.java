package net.chimhaha.clone.service;

import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.menu.MenuRepository;
import net.chimhaha.clone.web.dto.menu.MenuFindResponseDto;
import net.chimhaha.clone.web.dto.menu.MenuSaveRequestDto;
import net.chimhaha.clone.web.dto.menu.MenuUpdateRequestDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    public void 메뉴_등록() {
        // given
        Menu menu = Menu.builder()
                .name("침하하")
                .build();
        ReflectionTestUtils.setField(menu, "id", 1L);

        MenuSaveRequestDto dto = MenuSaveRequestDto.builder()
                .name("침하하")
                .build();

        given(menuRepository.save(any(Menu.class)))
                .willReturn(menu);

        // when
        Long savedMenuId = menuService.save(dto);

        // then
        assertAll(
                () -> assertEquals(1L, savedMenuId),
                () -> verify(menuRepository, times(1)).save(any(Menu.class))
        );
    }

    /* 추후 dtoList내의 board까지 검증하도록 수정 */
    @Test
    public void 메뉴_전체_조회() {
        // given
        String[] list = {"침하하", "전체글", "침착맨", "웃음", "운동"};
        List<Menu> menuList = new LinkedList<>();

        for(int i = 0; i < 5; i++) {
            menuList.add(Menu.builder()
                    .name(list[i])
                    .build());
        }

        given(menuRepository.findAll())
                .willReturn(menuList);

        // when
        List<MenuFindResponseDto> dtoList = menuService.find();

        // then
        assertAll(
                () -> assertEquals(5, dtoList.size()),
                () -> assertEquals("침하하", dtoList.get(0).getName()),
                () -> assertEquals("전체글", dtoList.get(1).getName()),
                () -> assertEquals("침착맨", dtoList.get(2).getName()),
                () -> assertEquals("웃음", dtoList.get(3).getName()),
                () -> assertEquals("운동", dtoList.get(4).getName())
        );
    }

    @Test
    public void 메뉴_수정() {
        // given
        Menu menu = Menu.builder()
                .name("운동")
                .build();
        ReflectionTestUtils.setField(menu, "id", 1L);

        MenuUpdateRequestDto dto = MenuUpdateRequestDto.builder()
                .name("운동")
                .build();

        given(menuRepository.findById(any(Long.class)))
                .willReturn(Optional.ofNullable(menu));

        // when
        Long updatedMenuId = menuService.update(1L, dto);

        // then
        assertAll(
                () -> assertEquals(1L, updatedMenuId),
                () -> verify(menuRepository, times(1)).findById(any(Long.class))
        );
    }

    @Test
    public void 메뉴_삭제() {
        // given

        doNothing().when(menuRepository).deleteById(any(Long.class));

        // when
        menuService.delete(1L);

        // then
        assertAll(
                () -> verify(menuRepository, times(1)).deleteById(any(Long.class))
        );

    }
}
