package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.menu.MenuRepository;
import net.chimhaha.clone.web.dto.menu.MenuFindResponseDto;
import net.chimhaha.clone.web.dto.menu.MenuSaveRequestDto;
import net.chimhaha.clone.web.dto.menu.MenuUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    @Transactional
    public Long save(MenuSaveRequestDto dto) {
        Menu menu = menuRepository.save(Menu.builder()
                .name(dto.getName())
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

    @Transactional
    public Long update(Long id, MenuUpdateRequestDto dto) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 해당 메뉴를 찾을 수 없습니다."));

        menu.update(dto.getName());

        return menu.getId();
    }

    @Transactional
    public void delete(Long id) {
        menuRepository.deleteById(id);
    }

    /* 서비스 계층 내에서만 사용할 메소드들 */
    @Transactional(readOnly = true)
    Menu findById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 메뉴를 찾을 수 없습니다. id=" + id));
    }
}
