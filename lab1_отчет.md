
### 1) Выбрана для внедрения уязвимость Open redirect
### 2) Внедрение уязвимости:

Добавлен следующий метод в OrderController.java:

```java
@GetMapping("/orders/redirect")
    public RedirectView redirect(@RequestParam String url) {
        return new RedirectView(url, false);
    }
```

![[Pasted image 20261006202654.png]]


### 3) fix уязвимости:

Добавлен следующий метод в OrderController.java:

```java
@GetMapping("/orders/safe-redirect")
    public RedirectView safeRedirect(@RequestParam String url) {
        // Разрешаем только относительные пути (начинаются с /)
        if (url.startsWith("/") && !url.startsWith("//")) {
            return new RedirectView(url, true);
        }
        // Если URL внешний или подозрительный — редиректим на главную страницу
        return new RedirectView("/", true);
    }
```

![[Pasted image 20261006203523.png]]
