package cn.itcast.bookstore.book.web.servlet.admin;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.bookstore.book.domain.Book;
import cn.itcast.bookstore.book.service.BookService;
import cn.itcast.bookstore.category.domain.category;
import cn.itcast.bookstore.category.service.categoryService;
import cn.itcast.commons.CommonUtils;

public class AdminAddBookServlet extends HttpServlet {
	private BookService bookService = new BookService();
	private categoryService categoryService = new categoryService();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		/*
		 * 1. 把表单数据封装到Book对象中
		 *   * 上传三步
		 */
		// 创建工厂
		DiskFileItemFactory factory = new DiskFileItemFactory(15 * 1024, new File("F:/f/temp"));
		// 得到解析器
		ServletFileUpload sfu = new ServletFileUpload(factory);
		// 设置单个文件大小为15KB
		sfu.setFileSizeMax(20 * 1024);
		// 使用解析器去解析request对象，得到List<FileItem>
		try {
			List<FileItem> fileItemList = sfu.parseRequest(request);
			/*
			 * * 把fileItemList中的数据封装到Book对象中
			 *   > 把所有的普通表单字段数据先封装到Map中
			 *   > 再把map中的数据封装到Book对象中
			 */
			Map<String,String> map = new HashMap<String,String>();
			for(FileItem fileItem : fileItemList) {
				if(fileItem.isFormField()) {
					map.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
				}
			}

			Book book = CommonUtils.toBean(map, Book.class);
			// 为book指定bid
			book.setBid(CommonUtils.uuid());
			/*
			 * 需要把Map中的cid封装到Category对象中，再把Category赋给Book
			 */
			category category = CommonUtils.toBean(map, category.class);
			book.setCategory(category);
			
			/*
			 * 2. 保存上传的文件
			 *   * 保存的目录
			 *   * 保存的文件名称
			 */
			// 得到保存的目录
			String savepath = this.getServletContext().getRealPath("/book_img");
			// 得到文件名称：给原来文件名称添加uuid前缀！避免文件名冲突
			String prename =fileItemList.get(1).getName();
			if(prename.lastIndexOf("\\")!=-1){
				//解决不同浏览器获取的路径不同
				prename=prename.substring(prename.lastIndexOf("\\")+1);
			}
			String filename = CommonUtils.uuid() + "_" + prename;
			
			
			/*
			 * 校验文件的扩展名
			 */
			if(!filename.toLowerCase().endsWith("jpg")) {
				request.setAttribute("msg", "您上传的图片不是JPG扩展名！");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
						.forward(request, response);
				return;
			}
			
			
			// 使用目录和文件名称创建目标文件
			File destFile = new File(savepath, filename);
			// 保存上传文件到目标文件位置
			fileItemList.get(1).write(destFile);
			
			/*
			 * 3. 设置Book对象的image，即把图片的路径设置给Book的image
			 */
			book.setImage("book_img/" + filename);
			
			
			/*
			 * 4. 使用BookService完成保存
			 */
			bookService.add(book);
			
			
			/*
			 * 校验图片的尺寸
			 */
			Image image = new ImageIcon(destFile.getAbsolutePath()).getImage();
			if(image.getWidth(null) > 200 || image.getHeight(null) > 200) {
				destFile.delete();//删除这个文件！
				request.setAttribute("msg", "您上传的图片尺寸超出了200 * 200！");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
						.forward(request, response);
				return;
			}
			
			
			/*
			 * 5. 返回到图书列表
			 */
			request.getRequestDispatcher("/admin/AdminBookServlet?method=findAll")
					.forward(request, response);
		} catch (Exception e) { 
			if(e instanceof FileUploadBase.FileSizeLimitExceededException) {
				request.setAttribute("msg", "您上传的文件超出了15KB");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
						.forward(request, response);
			}
		}
	}
}

