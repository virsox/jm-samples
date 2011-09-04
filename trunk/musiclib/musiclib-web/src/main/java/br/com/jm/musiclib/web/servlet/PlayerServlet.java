package br.com.jm.musiclib.web.servlet;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
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
 * Servlet implementation class PlayerServlet
 */
@WebServlet(name = "Player", urlPatterns = { "/Player" })
public class PlayerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private Player player;

	@Inject
	private MusicService musicService;
	
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PlayerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Music music = player.next();
		MusicFile file = musicService.getMusicFile(music.getFileId());
			
		response.setContentType("audio/mp3");
		if (music != null) {

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
		// TODO incrementar quantas vezes a musica foi tocada

	}

}
