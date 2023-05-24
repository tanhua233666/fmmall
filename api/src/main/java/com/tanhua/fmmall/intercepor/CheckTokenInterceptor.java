package com.tanhua.fmmall.intercepor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tanhua.fmmall.vo.ResultCode;
import com.tanhua.fmmall.vo.ResultVO;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CheckTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        System.out.println("拦截器启动：" + token);
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())){
            return true;
        }

        if (token == null){
            //打回请求
            ResultVO resultVO = new ResultVO(ResultCode.USER_LOGIN_NOT,null);
            doResponse(response,resultVO);
        }else {
            JwtParser parser = Jwts.parser();
            parser.setSigningKey("y1000z8g");  //必须和生成时的密码一致

            //如果token正确（密码符合、有效期内）正常执行，否则抛异常
            try {
                Jws<Claims> claimsJws = parser.parseClaimsJws(token);
                return true;
            }catch (ExpiredJwtException e) {
                ResultVO resultVO = new ResultVO(ResultCode.USER_LOGIN_OVERDUE,null);
                doResponse(response,resultVO);
            }catch (UnsupportedJwtException e) {
                ResultVO resultVO = new ResultVO(110,"Token不合法，请自重",null);
                doResponse(response,resultVO);
            }catch (Exception e){
                ResultVO resultVO = new ResultVO(ResultCode.USER_LOGIN_NOT,null);
                doResponse(response,resultVO);
            }
        }
        return false;
    }

    private void doResponse(HttpServletResponse response,ResultVO resultVO) throws IOException {
        //response响应ajax
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String s = new ObjectMapper().writeValueAsString(resultVO);
        out.print(s);
        out.flush();
        out.close();
    }
}
