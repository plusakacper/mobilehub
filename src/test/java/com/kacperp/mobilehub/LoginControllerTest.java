package com.kacperp.mobilehub;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kacperp.mobilehub.controller.LoginController;
import com.kacperp.mobilehub.dto.UserDto;
import com.kacperp.mobilehub.model.User;
import com.kacperp.mobilehub.service.IOrderProductService;
import com.kacperp.mobilehub.service.IProductCategoryService;
import com.kacperp.mobilehub.service.IUserService;

import java.util.Optional;

import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.util.NestedServletException;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private IProductCategoryService productCategoryService;
    @Mock
    private IOrderProductService orderProductService;
    @Mock
    private IUserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new LoginController(productCategoryService, orderProductService, userService)).build();
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void testRegister() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterAction_withValidUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("user", userDto))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testRegisterAction_withExistingUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");

        User existingUser = new User();
        existingUser.setEmail("test@test.com");

        when(userService.findByEmail(userDto.getEmail())).thenReturn(Optional.of(existingUser));

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("user", userDto))
                .andExpect(status().isOk());
    }

//    @Test
//    void testRegisterAction_withInvalidUser() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setEmail("invalid-email");
//
//        BindingResult bindingResult = mock(BindingResult.class);
//        when(bindingResult.hasErrors()).thenReturn(true);
//
//        NestedServletException exception = assertThrows(NestedServletException.class, () -> {
//            mockMvc.perform(post("/register")
//                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                    .flashAttr("user", userDto)
//                    .flashAttr("org.springframework.validation.BindingResult.user", bindingResult));
//        });
//
//        assertTrue(exception.getCause() instanceof ConstraintViolationException);
//    }
}
