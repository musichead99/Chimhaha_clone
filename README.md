# ReadMe

1. 개요
2. 기술 스택
3. 개발일정
4. ToDos
5. ERD
6. API스펙

&nbsp;
## ✏ 개요

&nbsp;침하하 클론코딩 프로젝트는 유튜버 침착맨님의 팬 페이지 `침하하`를 직접 분석하여 웹 커뮤니티 서버를 만들어보는 프로젝트입니다. 다른 프로젝트나 클론코딩 관련 강의들을 참고하지 않았으며, 일반 커뮤니티 개발 프로젝트와 크게 다른 점은 없습니다. 
&nbsp;그럼에도 불구하고 침하하 클론코딩이라는 이름을 사용한 이유는 개발의 동기부여를 위해서입니다.

&nbsp; 웹 커뮤니티 서버를 구현하며 기본적인 CRUD, 소셜 로그인을 통한 유저의 인증 및 인가, 테스트코드 작성, 디자인패턴 적용을 통한 객체 지향적 코드 작성, 깃 커밋 메시지 활용하기, redis를 이용한 캐싱 등을 목표로 합니다.

&nbsp;
## 💻 기술 스택

| 분류 | 사용한 기술 |
| ------------ | ------------- |
| **Language** | ![](https://img.shields.io/badge/Java-007396?style=flat&logo=OpenJDK&logoColor=white) ![](https://img.shields.io/badge/SQL-4479A1?style=flat&logo=MySQL&logoColor=white) |
| **Framework** | ![](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat&logo=Spring%20Boot&logoColor=white) |
| **DB** | ![](https://img.shields.io/badge/H2-4053D6?style=flat&logo=Amazon%20DynamoDB&logoColor=white) |

&nbsp;
## 🗓 개발일정

1. **2023.03 ~ 2023.06**
    * Posts(게시글) 객체 CRUD 작성 및 테스트 코드 작성
    * Category(말머리) 객체 CRUD 작성 및 테스트 코드 작성
    * Boards(게시판) 객체 CRUD 작성 및 테스트 코드 작성
    * Posts 조회 API에 페이징 적용
    * Menu(상단 메뉴) 객체 CRUD 작성 및 테스트 코드 작성
    * Comments(댓글) 객체 CRUD 작성 및 테스트 코드 작성
    * Comments에 대댓글 기능, 페이징 적용
    * Images(게시글 첨부 이미지) 객체 작성, 게시글에 이미지 첨부 기능 추가, 테스트 코드 작성
    * `ControllerAdvice`를 이용한 에러 핸들링 기능 적용, 커스텀 예외 적용
    * `Spring OAuth2 Client`를 사용한 Naver 소셜 로그인 적용
&nbsp;

2. **2023.09 ~ ing**
    * Service 계층에 파사드 패턴 적용

&nbsp;
## 💼 ToDos
* ~~Service 계층에 파사드 패턴 적용~~
* 게시글 좋아요 기능 추가
* 좋아요, 조회수 등 공유 자원들에 대한 동시성 이슈 테스트(Jmeter 사용해볼 생각)
* redis를 적용해 게시글 단건조회, 메뉴, 게시판, 카테고리 조회 시 캐싱 적용
* 게시글이나 댓글 작성 시 유저에게 포인트를 적립하는 기능 추가

## 🗃 ERD

![erd_cloud](https://github.com/musichead99/Chimhaha_clone/assets/76652013/d9479440-b0fb-43a3-9aaf-c5eea8c5df11)

&nbsp;
## API Spec

