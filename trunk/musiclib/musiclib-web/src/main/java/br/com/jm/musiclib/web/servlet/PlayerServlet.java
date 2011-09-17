package br.com.jm.musiclib.web.servlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.jm.musiclib.model.Music;
import br.com.jm.musiclib.model.MusicFile;
import br.com.jm.musiclib.model.MusicService;
import br.com.jm.musiclib.web.Player;

/**
 * Servlet responsável por fazer o stream da música
 */
@WebServlet(name = "Player", urlPatterns = { "/Player" })
public class PlayerServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        @Inject
        private Player player;

        @Inject
        private MusicService musicService;


        /**
         * Construtor padrão.
         * @see HttpServlet#HttpServlet()
         */
        public PlayerServlet() {
                super();
        }

        /**
         * Método GET que irá devolver o stream de bytes para o browser.
         * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
         *      response)
         */
        protected void doGet(HttpServletRequest request,
                        HttpServletResponse response) throws ServletException, IOException {
        		// Obtem a musica atual do servlet
                Music music = player.getCurrentMusic();
                // Pega os bytes da música
                MusicFile file = musicService.getMusicFile(music.getFileId());
                // Informa que estamos mandando um audio/mp3
                response.setContentType("audio/mp3");
                if (music != null) {
                		// obtem o output stream e devolve para o browser
                        OutputStream out = response.getOutputStream();
                        BufferedInputStream in = new BufferedInputStream(
                                        file.getInputStream());
                        int b;
                        while ((b = in.read()) != -1) {
                                out.write(b);
                        }

                        in.close();
                        out.close();
                }

        }

}