package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.domain.member.MemberRole;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.menu.MenuRepository;
import net.chimhaha.clone.exception.CustomException;
import net.chimhaha.clone.exception.ErrorCode;
import net.chimhaha.clone.dto.menu.MenuFindResponseDto;
import net.chimhaha.clone.dto.menu.MenuSaveRequestDto;
import net.chimhaha.clone.dto.menu.MenuUpdateRequestDto;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MemberService memberService;

    @Secured({MemberRole.ROLES.ADMIN})
    @Transactional
    public Long save(MenuSaveRequestDto dto, Long memberId) {
        Member member = memberService.findById(memberId);
        Menu menu = menuRepository.save(Menu.builder()
                .name(dto.getName())
                .member(member)
                .build());

        return menu.getId();
    }

    @Transactional(readOnly = true)
    public List<MenuFindResponseDto> find() {
        List<Menu> menu = menuRepository.findAll();

        return menu.stream()
                .map(MenuFindResponseDto::from)
                .collect(Collectors.toList());
    }

    @Secured({MemberRole.ROLES.ADMIN})
    @Transactional
    public Long update(Long id, MenuUpdateRequestDto dto) {
        Menu menu = this.findById(id);

        menu.update(dto.getName());

        return menu.getId();
    }

    @Secured({MemberRole.ROLES.ADMIN})
    @Transactional
    public void delete(Long id) {
        menuRepository.deleteById(id);
    }

    /* 서비스 계층 내에서만 사용할 메소드들 */
    @Transactional(readOnly = true)
    public Menu findById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }
}
