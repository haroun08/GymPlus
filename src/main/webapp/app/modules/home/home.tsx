import React from 'react';
import { Link } from 'react-router-dom';
import { Container, Grid, Typography, Button, useMediaQuery } from '@mui/material';
import { styled } from '@mui/system';
import { motion } from 'framer-motion';
import AnchorLink from 'react-anchor-link-smooth-scroll';
import HText from './HText'; // Assuming HText component is defined properly
import './home.scss'; // Assuming this file contains additional custom styles

const StyledContainer = styled(Container)(({ theme }) => ({
  padding: theme.spacing(4, 0),
}));

const Section = styled('section')({
  marginBottom: '80px',
});

const Header = styled('div')({
  textAlign: 'center',
  marginBottom: '40px',
});

const ImageContainer = styled('div')({
  position: 'relative',
  marginBottom: '20px',
  maxWidth: '800px',
  margin: '0 auto',
});

const StyledImage = styled('img')({
  maxWidth: '100%',
});

const InfoOverlay = styled('div')({
  position: 'absolute',
  top: 0,
  left: 0,
  width: '100%',
  padding: '20px',
  backgroundColor: 'rgba(0, 0, 0, 0.7)',
  color: '#fff',
  opacity: 0,
  transition: 'opacity 0.3s ease-in-out',
  '&:hover': {
    opacity: 1,
  },
});

const DescriptionContent = styled('div')({
  flex: '1',
  marginLeft: '20px',
});

const Description = styled('div')({
  textAlign: 'center',
  fontSize: '1.25rem',
  margin: '0 auto 40px',
  maxWidth: '800px',
});

const CTA = styled('div')({
  textAlign: 'center',
  marginTop: '50px',
});

const Links = styled('div')({
  textAlign: 'center',
  marginTop: '50px',
});

const LinkContainer = styled('div')({
  marginBottom: '30px',
  '&:last-child': {
    marginBottom: 0,
  },
});

const Home = () => {
  const isAboveMediumScreens = useMediaQuery('(min-width:1060px)');

  return (
    <StyledContainer>
      <motion.div
        initial={{ opacity: 0, y: 50 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        <Section>
          <Header>
            <Typography variant="h2" component="div">WELCOME TO GYM PLUS</Typography>
          </Header>
          <Description>
            <ImageContainer>
              <StyledImage src="/content/images/signin.png" alt="Sign in"/>
              <DescriptionContent>
                <Typography variant="body1" paragraph>
                  GYM PLUS TO THE BEST WAY TO PRACTICE!
                </Typography>
              </DescriptionContent>
            </ImageContainer>
          </Description>
        </Section>
      </motion.div>
      <motion.div
        initial={{ opacity: 0, y: 50 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.2 }}
      >
        <Section>
          <CTA>
            <div className="relative">
              <div className="before:absolute before:-top-20 before:-left-20 before:z-[1] before:content-abstractwaves">
                <motion.div
                  initial="hidden"
                  whileInView="visible"
                  viewport={{ once: true, amount: 0.5 }}
                  transition={{ duration: 0.5 }}
                  variants={{
                    hidden: { opacity: 0, x: 50 },
                    visible: { opacity: 1, x: 0 },
                  }}
                >
                  <HText>
                    MILLIONS OF HAPPY MEMBERS GETTING{' '}
                    <span className="text-primary-500">FIT</span>
                  </HText>
                </motion.div>
              </div>
            </div>
            <ImageContainer>
              <StyledImage src="/content/images/image1.jpg" alt="Product"/>
              <DescriptionContent>
                <Typography variant="body1" paragraph>
                  Ready to get started? Explore our products now.
                </Typography>
                <AnchorLink href="#product-section" offset="50">
                  <Button variant="contained" color="primary" sx={{ marginRight: 2 }}>
                    Explore Now
                  </Button>
                </AnchorLink>
              </DescriptionContent>
            </ImageContainer>
          </CTA>
        </Section>
      </motion.div>
      <motion.div
        initial={{ opacity: 0, y: 50 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.4 }}
      >
        <Section id="product-section">
          <Links>
            <Grid container justifyContent="center" spacing={4}>
              {linkItems.map((link, index) => (
                <Grid item xs={12} md={6} key={index}>
                  <LinkContainer>
                    <Typography variant="h4" gutterBottom>{link.title}</Typography>
                    <ImageContainer>
                      <StyledImage src={link.image} alt={link.title}/>
                      <DescriptionContent>
                        <Typography variant="body1" paragraph>{link.description}</Typography>
                        <Button component={Link} to={link.to} variant="contained" color="primary">
                          {link.buttonText}
                        </Button>
                      </DescriptionContent>
                    </ImageContainer>
                  </LinkContainer>
                </Grid>
              ))}
            </Grid>
          </Links>
        </Section>
      </motion.div>
    </StyledContainer>
  );
};

// Data for link items
const linkItems = [
  {
    title: 'Category',
    image: '/content/images/image2.jpg',
    description: 'Browse our categories and find what you need.',
    to: '/category',
    buttonText: 'Go to Category',
  },
  {
    title: 'Gym',
    image: '/content/images/image3.jpg',
    description: 'Explore our gym locations and facilities.',
    to: '/gym',
    buttonText: 'Go to Gym',
  },
];

export default Home;
