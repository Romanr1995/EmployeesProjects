package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.consulting.dto.DepartmentDto;
import ru.consulting.service.DepartmentService;
import ru.consulting.validated.MapValidConstraint;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("department")
public class DepartmentController {

    private DepartmentService departmentService;
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public void saveNew(@RequestBody @Valid DepartmentDto departmentDto) {
        departmentService.save(departmentDto);
    }

    @GetMapping
    public List<DepartmentDto> showAll() {
        return departmentService.getAll();
    }

//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,
//            consumes = MediaType.ALL_VALUE,
//            headers = "ttt")
//    public List<DepartmentDto> showAllDepartment(HttpServletRequest httpServletRequest,
//                                                 HttpServletResponse httpServletResponse) {
//        EmployeeDto forObject = restTemplate.getForObject("http://localhost:8080/employee/9", EmployeeDto.class);
//        System.out.println(forObject.getEmail());
//        restTemplate.delete("http://localhost:8080/employee/10");
//        System.out.println(restTemplate.optionsForAllow("http://localhost:8080/employee").size());

//        HttpEntity<DepartmentDto> request1 = new HttpEntity<>(new DepartmentDto().setTitle("Test67"));
//        HttpEntity<DepartmentDto> request2 = new HttpEntity<>(new DepartmentDto().setTitle("Test65"));
//        DepartmentDto departmentDto = restTemplate.postForObject("http://localhost:8080/department", request1, DepartmentDto.class);
//        URI uri = restTemplate.postForLocation("http://localhost:8080/department", request2);


//        httpServletResponse.addHeader("a","abc");
//       httpServletResponse.addCookie(new Cookie("q","qqq"));
//        System.out.println(httpServletRequest.getCookies()[0].getValue());
//        System.out.println(httpServletRequest.getMethod());
//        System.out.println(httpServletRequest.getCookies()[0].getValue());
//        System.out.println(httpServletRequest.getCookies()[0]);
//
//        HttpSession httpSession = httpServletRequest.getSession();
//        System.out.println(httpServletRequest.getLocalPort());
//
//        System.out.println(httpServletRequest.getAuthType());
//        System.out.println(httpServletRequest.getProtocol());

//        httpSession.setMaxInactiveInterval(30);
//        httpSession.setAttribute("1","a1");
//        System.out.println(httpSession.getCreationTime());
//        System.out.println(httpSession.getLastAccessedTime());
//        System.out.println(httpSession.getMaxInactiveInterval());
//        System.out.println(httpSession.isNew());
//        System.out.println(httpSession.getId());
//        System.out.println(httpSession.getAttribute("1"));
//        return departmentService.getAll();
//    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        departmentService.removeById(id);
    }

//    @PostMapping("/save/list")
//    public ResponseEntity<Void> saveNewList(@RequestBody List<@Valid DepartmentDto> departmentDtos) {
//        departmentService.saveAll(departmentDtos);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    @PutMapping("update/departmenthead/{id}")
    public void updateDepartmentHead(@PathVariable Long id,
                                     @RequestParam @Pattern(regexp = "89[0-9]{9}", message = "Телефонный номер должен начинаться с 89, затем - 9 цифр")
                                             String phone,
                                     @RequestParam(required = false) @Email String email) {
        departmentService.updateDepartmentHead(id, phone, email);
    }

    @PostMapping("savehigherdepartment")
    public void setHigherDepartment(@RequestBody @MapValidConstraint Map<String, DepartmentDto> departmentDtoMap) {
        departmentService.setHigherDepartment(departmentDtoMap);
    }

//    @RequestMapping(value = "/image", method = RequestMethod.GET, produces = "image/jpg")
//    public @ResponseBody byte[] getFile() {
//        try {
//            // Retrieve image from the classpath.
//            InputStream is = this.getClass().getResourceAsStream("/test.jpg");
//
//            // Prepare buffered image.
//            BufferedImage img = ImageIO.read(is);
//
//            // Create a byte array output stream.
//            ByteArrayOutputStream bao = new ByteArrayOutputStream();
//
//            // Write to output stream
//            ImageIO.write(img, "jpg", bao);
//
//            return bao.toByteArray();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
