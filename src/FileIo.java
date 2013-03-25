import java.io.*;

// Enum Singleton
enum FileIo {
	@SuppressWarnings("UnusedDeclaration")
	INSTANCE;

	public static String getDirFromPath(final String path) {
		String fileSep = "/";
		if (!path.contains(fileSep))
			fileSep = "\\";
		final int lastIdx = path.lastIndexOf(fileSep);
		if (lastIdx == -1)
			return "./";
		return path.substring(0, lastIdx+1);

	}

	public static String getFileFromPath(final String path) {
		String fileSep = "/";
		if (!path.contains(fileSep))
			fileSep = "\\";
		final int lastIdx = path.lastIndexOf(fileSep);
		if (lastIdx == -1)
			return path;
		return path.substring(lastIdx+1, path.length());

	}

	public static int getMaxFileIdx(final String path, final String fileName)
	{
		int maxIdx = 0;
		final String[] files = new File(path).list();
		if (files == null || files.length == 0)
			return maxIdx;
		for (String file : files)
		{

			if (file.startsWith(fileName))
			{
				if (file.equals(fileName))
				{
					maxIdx = 0;
				}
				else
				{
					String idxStr = file.substring(fileName.length()+1, file.length());
					try
					{
						int idx = Integer.parseInt(idxStr);
						if (idx > maxIdx)
							maxIdx = idx;
					}
					catch (Exception e)
					{

					}
				}
			}
		}
		return maxIdx;
	}
}