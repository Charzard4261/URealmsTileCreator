package charzard;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.imageio.ImageIO;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;

@SuppressWarnings("deprecation")
public class Startup {

	GUI g;
	private boolean dropbox = false;
	private DbxClientV2 client;

	public static void main(String[] args)
	{
		new GUI();
	}

	public Startup(String directory, boolean db, GUI g)
	{
		directory = directory.replace('\\', '/');
		dropbox = db;
		this.g = g;

		ArrayList<String> tilenames = new ArrayList<String>();
		// String jarFolder = new File(Startup.class.getProtectionDomain().getCodeSource()
		// .getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');

		if (dropbox)
		{

			final String APP_KEY = "qngwye2llp6k5tj";
			final String APP_SECRET = "g7m5lk3s1ex4o6d";

			DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

			DbxRequestConfig config = new DbxRequestConfig("URealms Tile Creator", Locale
					.getDefault().toString());
			DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

			// Have the user sign in and authorise your app.
			String authorizeUrl = webAuth.start();
			g.message("1. Go to: " + authorizeUrl);
			g.message("2. Click \"Allow\" (you might have to log in first)");
			g.message("3. Copy the authorization code.");

			// This will fail if the user enters an invalid authorisation code.
			DbxAuthFinish authFinish;
			try
			{
				String code = new BufferedReader(new InputStreamReader(System.in)).readLine()
						.trim();
				authFinish = webAuth.finish(code);
				String accessToken = authFinish.getAccessToken();
				client = new DbxClientV2(config, accessToken);
			} catch (DbxException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		for (File f : findimages(directory + "/"))
		{
			tilenames.add(f.getName());
			g.message("Starting " + f.getName());
			createTile(f);
		}
		g.message("Done");
		g.finish();
	}

	public File[] findimages(String dirName)
	{
		File dir = new File(dirName);

		return dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename)
			{
				return (filename.endsWith(".png") || filename.endsWith(".jpg"));
			}
		});

	}

	public void uploadFile(DbxClientV2 dbxClient, File localFile, String dropboxPath,
			ArrayList<String> tile)
	{
		try
		{
			InputStream in = new FileInputStream(localFile);

			dbxClient.files().uploadBuilder(dropboxPath).withMode(WriteMode.OVERWRITE)
					.withClientModified(new Date(localFile.lastModified())).uploadAndFinish(in);

			SharedLinkMetadata sharedLinkMetadata = client.sharing().createSharedLinkWithSettings(
					dropboxPath);
			tile.add(sharedLinkMetadata.getUrl().substring(0,
					sharedLinkMetadata.getUrl().length() - 1)
					+ "1");

			in.close();

			g.message("Uploaded " + localFile.getName());
		} catch (UploadErrorException ex)
		{
			g.message("Error uploading to Dropbox: " + ex.getMessage());
		} catch (DbxException ex)
		{
			g.message("Error uploading to Dropbox: " + ex.getMessage());
		} catch (IOException ex)
		{
			g.message("Error reading from file \"" + localFile + "\": " + ex.getMessage());
		}
	}

	public void createTile(File f)
	{
		String tilename = f.getName();
		tilename = tilename.substring(0, tilename.length() - 4);
		boolean success = (new File(tilename)).mkdirs();
		if (!success)
		{
			// Directory creation failed
		}

		File tileBlank = new File(tilename + "/" + tilename + "_1.png");
		File tileBlinded = new File(tilename + "/" + tilename + "_2.png");
		File tileBurned = new File(tilename + "/" + tilename + "_4.png");
		File tileCharmed = new File(tilename + "/" + tilename + "_5.png");
		File tileDanger = new File(tilename + "/" + tilename + "_9.png");
		File tileFrozen = new File(tilename + "/" + tilename + "_7.png");
		File tilePoisoned = new File(tilename + "/" + tilename + "_3.png");
		File tileSilenced = new File(tilename + "/" + tilename + "_6.png");
		File tileStunned = new File(tilename + "/" + tilename + "_8.png");

		try
		{
			BufferedImage base = ImageIO.read(Startup.class
					.getResource("effects/tileDesign Blank.png"));

			BufferedImage face = ImageIO.read(f);

			face = Utils.rotate180(face);
			face = Utils.createResizedCopy(face, 425, 425, false); // old - 283

			int offset = 4; // old = 3

			base.getGraphics().drawImage(face, offset, offset, null);

			base.getGraphics().drawImage(
					ImageIO.read(Startup.class.getResource("effects/tileDesign Blank.png")), 0, 0,
					null);
			ImageIO.write(base, "png", tileBlank);

			base.getGraphics().drawImage(face, offset, offset, null);

			base.getGraphics().drawImage(
					ImageIO.read(Startup.class.getResource("effects/tileDesign Blinded.png")), 0,
					0, null);
			ImageIO.write(base, "png", tileBlinded);

			base.getGraphics().drawImage(face, offset, offset, null);

			base.getGraphics().drawImage(
					ImageIO.read(Startup.class.getResource("effects/tileDesign Burned.png")), 0, 0,
					null);
			ImageIO.write(base, "png", tileBurned);

			base.getGraphics().drawImage(face, offset, offset, null);

			base.getGraphics().drawImage(
					ImageIO.read(Startup.class.getResource("effects/tileDesign Charmed.png")), 0,
					0, null);
			ImageIO.write(base, "png", tileCharmed);

			base.getGraphics().drawImage(face, offset, offset, null);

			base.getGraphics().drawImage(
					ImageIO.read(Startup.class.getResource("effects/tileDesign Danger.png")), 0, 0,
					null);
			ImageIO.write(base, "png", tileDanger);

			base.getGraphics().drawImage(face, offset, offset, null);

			base.getGraphics().drawImage(
					ImageIO.read(Startup.class.getResource("effects/tileDesign Frozen.png")), 0, 0,
					null);
			ImageIO.write(base, "png", tileFrozen);

			base.getGraphics().drawImage(face, offset, offset, null);

			base.getGraphics().drawImage(
					ImageIO.read(Startup.class.getResource("effects/tileDesign Poisoned.png")), 0,
					0, null);
			ImageIO.write(base, "png", tilePoisoned);

			base.getGraphics().drawImage(face, offset, offset, null);

			base.getGraphics().drawImage(
					ImageIO.read(Startup.class.getResource("effects/tileDesign Silenced.png")), 0,
					0, null);
			ImageIO.write(base, "png", tileSilenced);

			base.getGraphics().drawImage(face, offset, offset, null);

			base.getGraphics().drawImage(
					ImageIO.read(Startup.class.getResource("effects/tileDesign Stunned.png")), 0,
					0, null);
			ImageIO.write(base, "png", tileStunned);

			g.message("Created " + f.getName());
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		if (dropbox)
		{

			ArrayList<String> tile = new ArrayList<String>();

			uploadFile(client, tileBlank, "/" + tilename + "/" + tileBlank.getName(), tile);

			uploadFile(client, tileBlinded, "/" + tilename + "/" + tileBlinded.getName(), tile);

			uploadFile(client, tileBurned, "/" + tilename + "/" + tileBurned.getName(), tile);

			uploadFile(client, tileCharmed, "/" + tilename + "/" + tileCharmed.getName(), tile);

			uploadFile(client, tileDanger, "/" + tilename + "/" + tileDanger.getName(), tile);

			uploadFile(client, tileFrozen, "/" + tilename + "/" + tileFrozen.getName(), tile);

			uploadFile(client, tilePoisoned, "/" + tilename + "/" + tilePoisoned.getName(), tile);

			uploadFile(client, tileSilenced, "/" + tilename + "/" + tileSilenced.getName(), tile);

			uploadFile(client, tileStunned, "/" + tilename + "/" + tileStunned.getName(), tile);

			try
			{
				Path path = Paths.get(ExportResource("example.json", tilename));
				Charset charset = StandardCharsets.UTF_8;

				String content = new String(Files.readAllBytes(path), charset);
				content = content.replaceAll(
						"http://www.buffalowizards.com/urlive/tabletop/tiles/lilliandupree_1.jpg",
						tile.get(0));
				content = content.replaceAll(
						"http://www.buffalowizards.com/urlive/tabletop/tiles/lilliandupree_2.jpg",
						tile.get(1));
				content = content.replaceAll(
						"http://www.buffalowizards.com/urlive/tabletop/tiles/lilliandupree_3.jpg",
						tile.get(2));
				content = content.replaceAll(
						"http://www.buffalowizards.com/urlive/tabletop/tiles/lilliandupree_4.jpg",
						tile.get(6));
				content = content.replaceAll(
						"http://www.buffalowizards.com/urlive/tabletop/tiles/lilliandupree_5.jpg",
						tile.get(3));
				content = content.replaceAll(
						"http://www.buffalowizards.com/urlive/tabletop/tiles/lilliandupree_6.jpg",
						tile.get(7));
				content = content.replaceAll(
						"http://www.buffalowizards.com/urlive/tabletop/tiles/lilliandupree_7.jpg",
						tile.get(5));
				content = content.replaceAll(
						"http://www.buffalowizards.com/urlive/tabletop/tiles/lilliandupree_8.jpg",
						tile.get(8));
				content = content.replaceAll(
						"http://www.buffalowizards.com/urlive/tabletop/tiles/lilliandupree_9.jpg",
						tile.get(4));
				Files.write(path, content.getBytes(charset));
			} catch (Exception e)
			{
				e.printStackTrace();
			}

		}

	}

	/**
	 * Export a resource embedded into a Jar file to the local file path.
	 *
	 * @param resourceName
	 *            ie.: "/SmartLibrary.dll"
	 * @return The path to the exported resource
	 * @throws Exception
	 */
	static public String ExportResource(String resourceName, String tilename) throws Exception
	{
		InputStream stream = null;
		OutputStream resStreamOut = null;
		String jarFolder;
		try
		{
			stream = Startup.class.getResourceAsStream(resourceName);// note that each / is a
																		// directory down in the
																		// "jar tree" been the jar
																		// the root of the tree
			if (stream == null)
			{
				throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
			}

			int readBytes;
			byte[] buffer = new byte[4096];
			jarFolder = new File(Startup.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI().getPath()).getParentFile().getPath().replace('\\', '/');
			resStreamOut = new FileOutputStream(jarFolder + "/" + tilename + ".json");
			while ((readBytes = stream.read(buffer)) > 0)
			{
				resStreamOut.write(buffer, 0, readBytes);
			}
			resStreamOut.close();
		} catch (Exception ex)
		{
			throw ex;
		}

		return jarFolder + "/" + tilename + ".json";
	}
}
