import React from 'react';
import { Container, Grid, IconButton, Typography } from '@mui/material';
import FacebookIcon from '@mui/icons-material/Facebook';
import TwitterIcon from '@mui/icons-material/Twitter';
import GitHubIcon from '@mui/icons-material/GitHub';
import YouTubeIcon from '@mui/icons-material/YouTube';
import { LocaleMenu } from 'app/shared/layout/menus';
import './footer.scss'; // Import footer.scss

interface IFooterProps {
  currentLocale: string;
  handleLocaleChange: (newLocale: string) => void;
}

const Footer: React.FC<IFooterProps> = ({ currentLocale, handleLocaleChange }) => {
  const date = new Date().getFullYear();

  return (
    <footer className="footer">
      {' '}
      {/* Apply footer class */}
      <Container>
        <Grid container spacing={3} alignItems="center">
          <Grid item xs={12} md={6}>
            <Typography variant="body1">&copy; {date} GymPlus. All rights reserved.</Typography>
            <div className="locale-menu-wrapper">
              {' '}
              {/* Wrap LocaleMenu with a div */}
              <LocaleMenu currentLocale={currentLocale} onClick={handleLocaleChange} />
            </div>
          </Grid>
          <Grid item xs={12} md={6} container justifyContent="flex-end" alignItems="center">
            <Typography variant="body2" style={{ marginRight: '10px' }}>
              Follow us:
            </Typography>
            <IconButton component="a" href="https://www.facebook.com/haroun.brh.ar/">
              <FacebookIcon />
            </IconButton>
            <IconButton component="a" href="https://twitter.com">
              <TwitterIcon />
            </IconButton>
            <IconButton component="a" href="https://github.com/haroun08">
              <GitHubIcon />
            </IconButton>
            <IconButton component="a" href="https://www.youtube.com/@KunHaroun">
              <YouTubeIcon />
            </IconButton>
          </Grid>
        </Grid>
      </Container>
    </footer>
  );
};

export default Footer;
