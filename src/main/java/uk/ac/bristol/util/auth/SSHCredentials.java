package uk.ac.bristol.util.auth;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import lombok.Getter;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.jgit.util.FS;
import uk.ac.bristol.util.GitInfo;

/** A class for managing SSH credentials. */
public final class SSHCredentials implements Credentials, Externalizable {

  /** The name of this set of credentials. */
  private String id;
  /** The path to the ssh to be used. */
  private String path;
  /** The password for this set of credentials. */
  private String passphrase;
  /** The SSH callback authentication provider. */
  @Getter private TransportConfigCallback auth;

  /** Should only be accessed through reflection. */
  public SSHCredentials() {
    /* yeee */
  }

  /**
   * Contstruct a new set of SSH credentials.
   *
   * @param id The id for these credentials
   * @param path The path for these credentials
   * @param passphrase The passphrase for these credentials
   */
  public SSHCredentials(final String id, final String path, final String passphrase) {
    this.id = id;
    this.path = path;
    this.passphrase = passphrase;
    generateCallback();
  }

  /** Generate and assign the callback for auth. */
  void generateCallback() {
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
  public void reimport() {
    GitInfo.addSSH(id, path, passphrase);
  }

  /** {@inheritDoc} */
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    out.writeUTF(id);
    out.writeUTF(path);
    out.writeUTF(passphrase);
  }

  /** {@inheritDoc} */
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    id = in.readUTF();
    path = in.readUTF();
    passphrase = in.readUTF();
    generateCallback();
  }
}
