package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.domain.member.MemberRole;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.dto.menu.MenuFindResponseDto;
import net.chimhaha.clone.dto.menu.MenuSaveRequestDto;
import net.chimhaha.clone.dto.menu.MenuUpdateRequestDto;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 전체적으로 서비스들을 총괄하는 파사드 패턴을 적용한 객체
 * CRUD를 통해 가져온 도메인들을 DTO로 변환하거나 다른 서비스에서 필요한 객체를 조회하는 등
 * 실제 서비스 객체들이 하지 않아도 될 일을 분리하기 위해서 작성하기로 함
 * 이로서 각 서비스 객체들은 다른 서비스에 대한 의존성을 가지지 않아도 됨
 */

@RequiredArgsConstructor
@Service
public class CommunityService {

    private final MenuService menuService;
    private final BoardsService boardsService;
    private final CategoryService categoryService;
    private final MemberService memberService;
    private final PostsService postsService;

    @Secured({MemberRole.ROLES.ADMIN})
    @Transactional
    public Long saveMenu(MenuSaveRequestDto dto, Long memberId) {
        Member member = memberService.findById(memberId);
        return menuService.save(dto, member);
    }

    @Transactional(readOnly = true)
    public List<MenuFindResponseDto> findMenu() {
        List<Menu> menu = menuService.find();

        return menu.stream()
                .map(MenuFindResponseDto::from)
                .collect(Collectors.toList());
    }

    @Secured({MemberRole.ROLES.ADMIN})
    @Transactional
    public Long updateMenu(Long id, MenuUpdateRequestDto dto) {
        return menuService.update(id, dto);
    }

    @Secured({MemberRole.ROLES.ADMIN})
    @Transactional
    public void deleteMenu(Long id) {
        menuService.delete(id);
    }
}
