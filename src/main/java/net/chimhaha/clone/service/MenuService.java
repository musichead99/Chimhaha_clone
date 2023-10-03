package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.menu.MenuRepository;
import net.chimhaha.clone.exception.CustomException;
import net.chimhaha.clone.exception.ErrorCode;
import net.chimhaha.clone.dto.menu.MenuSaveRequestDto;
import net.chimhaha.clone.dto.menu.MenuUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    @Transactional
    public Long save(MenuSaveRequestDto dto, Member member) {
        Menu menu = menuRepository.save(Menu.builder()
                .name(dto.getName())
                .member(member)
                .build());

        return menu.getId();
    }

    @Transactional(readOnly = true)
    public List<Menu> find() {
        List<Menu> menu = menuRepository.findAll();

        return menu;
    }

    @Transactional
    public Long update(Long id, MenuUpdateRequestDto dto) {
        Menu menu = this.findById(id);

        menu.update(dto.getName());

        return menu.getId();
    }

    @Transactional
    public void delete(Long id) {
        menuRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Menu findById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }
}
