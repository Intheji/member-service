![java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![mySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
![gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![aws](https://img.shields.io/badge/Amazon_AWS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![DOCKER](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

# CH 4 클라우드_아키텍처 설계 & 배포
___
## LV 0
설정 완료된 AWS Budgets 화면
<img width="1645" height="377" alt="image" src="https://github.com/user-attachments/assets/625b38ca-bf25-4435-a909-272a598ad492" />


<details>
<summary><font color="#f08080 red">실습 후 요금 관리</font></summary>

🌟실습 끝났을 때 비용 안 나가게 하는 법 (중요)

실습 끝나면 이 2개만 삭제하세요

1️⃣ Amazon EC2 인스턴스 Terminate<br>
2️⃣ Amazon RDS DB 인스턴스 Delete

⚠️ AWS 실습에서 가장 많이 터지는 요금 사고

1️⃣ RDS 안 지우고 방치 (가장 흔함)<br>
2️⃣ NAT Gateway 생성 (시간당 요금)<br>
3️⃣ Elastic IP 미사용 상태
</details>

___
## LV 1
### 1. 설정 완료된 EC2의 퍼블릭 IP: `54.180.134.95`
### 2. [상태 검증 링크](http://54.180.134.95:8080/actuator/health)<br>
<img width="180" height="170" alt="image" src="https://github.com/user-attachments/assets/30c5a33a-d8df-4f4e-bcba-585bc1bc41c6" />

___
## LV 2
### 1. Actuator Info 엔드포인트 URL
[확인용 URL](http://54.180.134.95:8080/actuator/info)
<img width="245" height="127" alt="image" src="https://github.com/user-attachments/assets/fb683f05-d2a8-4bda-a6e4-03843abbf4a8" />
### 2. RDS 보안 그룹 스크린샷
<img width="2236" height="425" alt="image" src="https://github.com/user-attachments/assets/1287e636-f981-4945-90b6-72538fe5fb20" />

___
## LV 3
### 1. 발급받은 Presigned URL 1개와 해당 URL의 만료 시간
2026년 3월 18일 4시 34분에 Presigned URL 만료예정 <br>
[Presigned URL](https://member-service-profile.s3.ap-northeast-2.amazonaws.com/members/1/profile/582a4840-301d-40e4-9f8d-f068e6b090a0_KoAsXaqnGdy8MccsH5aWYVwkzy-jf4oNrHp-wyhv9Rx5B5qRL_9bjykEc9JASXcB90f5NyQ9llkHE9FUfAxEIA.webp?X-Amz-Security-Token=IQoJb3JpZ2luX2VjEIz%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEaDmFwLW5vcnRoZWFzdC0yIkgwRgIhAJnH8NHA9GmIKgax4jzd%2BwY%2BZc9bt1TRPD1qhDfbKX4yAiEAsTbZjT18OCmF8UiJanHbNOGlV3cor0yxsIiY0fGm8ogqyQUIVRAAGgwxNTUzMjYwNDk0NTkiDDeGT4fBD%2BmgYwXk8yqmBfS4KkVdWv45EgaCj6DEtqV8CTAP1v1iYfBY3ZeBrCooENwLkZVrCYbCC5VKB0UovW6hXtYiDpvmKstsXFL3XmftLeDebeSuuLALFS5nZQrm95exv4wG9TcPkV12nSBa30FmFwe3VpW1WSb9IxgVSsur76JW2DMgA1VwLgQGzM7oXTS2Z4My71c6BuqdbqkOo8Rpv5XbZhY%2BssfcjkKva9py1iz8KA0Hjr%2FDy56uxhGVd4JHDfdlacDvao4GHAbqBk%2FLCKv5pvXbiOkk%2BWXCdSw9%2F%2B67nqggTu8Bs2pzgt%2FU7Kc0g55opX49N1w5XF4R6GiikhNFjocg4s8PjuZZ1SSS%2Fz5kwFNUwCtg4MSr2SkLqmdmOnrIw3ahDTOhNCWrsiQEH8bhgc88NEyeRCg9a%2BZzKgrCbzCoDa55f20qiwHuVVaDrU6BZI9bOT6WlXQouXd2mnans880UXE718dk6q3niCOKK0flWkYK030nwLaCVthtj8Ji1an7oMZ4rwIy7sEF2rh5r3LFsprZ8%2BBqtA9gXG0njlCjHb0bnA4ULDZIBlynVJBTuSJjaCiqNcUx%2BRihQ%2FOKZPU14jaEZcNNm5LbZHLGUy%2B027iSuXoikbBjWRN2mweUvpUvLD%2BgMueCmcqmcFO8p1GM7gzs%2BAKdcw2PNK1QrlyYKVfwRUvjKnEURATovR41P2Bh%2FUwg%2FtYrop2Swmr6dNN33kFGHdp5z6%2BZ%2BWJiE3f3qG%2BFGLyKgkCbbqIfqHDg3pLp4r5HOXcJsacIk%2BDY5gJB5tPDlToZNbYXG6015QQKBcdeD%2FJaryxS8noR%2FADQKvwvooKXckmlK6g0g2MNFeqm5MNimunvnMAuScQn%2FOrnPn8idJVPBzGUFy%2Fhf0N60mGFm58iHC5ync4WhQxeTTD008PNBjqwAWMursgdD5Uaq4MZk%2Fw6elbFQu7QkUMdhnfZCj2Z2f7VDumjHIT4tKB1T63puYZb1Lp%2FedB%2FzC3NY1dGOs7BNvwbkIWXbG5nCwz%2BTt9ji0wUJy2jFtENqUInaD5k7%2FlQkqLwCEq6eP%2FxrwY4fx3seAMQXCgZXsgTcoTzGOEW1HW7sLgNYIE4rL3oHTKsj2ogeXFKDtYYBnqGyciqwUGPjFlTE1yUCWLH6CC%2B6%2BF250VF&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20260311T043401Z&X-Amz-SignedHeaders=host&X-Amz-Credential=ASIASIKRHNSZ2M5T5XPP%2F20260311%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Expires=604800&X-Amz-Signature=9999a44f4cb8e28b0fd11b18ed295c05bf225106f5c671f90a70d1b99f5a238e)

___
