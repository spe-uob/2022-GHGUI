package uk.ac.bristol.util.auth;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import lombok.Getter;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.jgit.util.FS;

/** A class for managing SSH credentials. */
public final class SSHCredentials implements ToByteStream {

  /** The path to the ssh to be used. */
  private String path;
  /** The password for this set of credentials. */
  private String passphrase;
  /** The SSH callback authentication provider. */
  @Getter private final TransportConfigCallback auth;

  /**
   * Contstruct a new set of SSH credentials.
   *
   * @param path The path for these credentials
   * @param passphrase The passphrase for these credentials
   */
  public SSHCredentials(final String path, final String passphrase) {
    this.path = path;
    this.passphrase = passphrase;
    this.auth =
        transport -> {
          if (transport instanceof SshTransport sshTransport) {
            // FOR USE WITH JSCH:
            final SshSessionFactory sshSessionFactory =
                new JschConfigSessionFactory() {
                  @Override
                  protected JSch createDefaultJSch(final FS fs) throws JSchException {
                    final JSch defaultJSch = super.createDefaultJSch(fs);
                    defaultJSch.addIdentity(path, passphrase);
                    return defaultJSch;
                  }
                };

            //// FOR USE WITH APACHE MINA:
            //
            // sshTransport.setCredentialsProvider(
            //     new UsernamePasswordCredentialsProvider("", passphrase));
            //
            // final File key = new File(path);
            // final FS fs = FS.detect();
            //
            // final SshdSessionFactory sshSessionFactory =
            //     new SshdSessionFactoryBuilder()
            //         .setHomeDirectory(fs.userHome())
            //         .setKeyPasswordProvider(IdentityPasswordProvider::new)
            //         .setSshDirectory(key.getParentFile())
            //         .setDefaultIdentities(__ -> Arrays.asList(key.toPath()))
            //         .build(null);

            sshTransport.setSshSessionFactory(sshSessionFactory);
          }
        };
  }

  /** {@inheritDoc} */
  @Override
  public byte[] toByteStream() {
    return String.format("http\0%s\0%s\0", path, passphrase).getBytes();
  }
}
