package com.security.security;

import com.sun.istack.NotNull;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{


    private JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtHelper jwtHelper, UserDetailsService userDetailsService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request
            , HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {
        String requestHeader=request.getHeader("Authorization");
      @NotNull String username=null;
      @NotNull String token=null;
        if(requestHeader!=null&&requestHeader.startsWith("Bearer")){

            token=requestHeader.substring(7);
            try{
                username=this.jwtHelper.getUsernameFromToken(token);
            }
            catch(IllegalArgumentException e){

                e.printStackTrace();
            }
            catch (ExpiredJwtException e){

                e.printStackTrace();
            }
            catch(MalformedJwtException e){

                e.printStackTrace();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtHelper.validationToken(token, userDetails);

            if (validateToken) {

                UsernamePasswordAuthenticationToken authenticationFilter = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationFilter.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationFilter);
            }
        }
filterChain.doFilter(request,response);

        }

    }

