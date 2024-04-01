import React from 'react';
import { Link } from 'react-router-dom';
import { Container, Row, Col } from 'reactstrap';
import { useAppSelector } from 'app/config/store';
import './home.scss';

const Home: React.FC = () => {
  const account = useAppSelector(state => state.authentication.account);
  const isDarkMode = true; // You can set this based on your application's dark mode state

  return (
    <div className={`home-container ${isDarkMode ? 'dark-mode' : ''}`}>
      <Container>
        <Row className="justify-content-center align-items-center">
          <Col lg="8">
            <div className="header">
              <h1>Welcome to Gym Plus</h1>
              <p>Your one-stop solution for fitness</p>
            </div>
          </Col>
        </Row>
        <Row className="justify-content-center align-items-center">
          <Col lg="8">
            <div className="image-container">
              <img src="/content/images/gymplus.jpg" alt="Gym Plus" className="gymplus-image" />
            </div>
          </Col>
        </Row>
        <Row className="justify-content-center align-items-center">
          <Col lg="8">
            <div className="description">
              <p>
                Gym Plus is your ultimate destination for all things fitness. Whether you're a seasoned athlete or
                just starting your fitness journey, we have everything you need to achieve your goals.
              </p>
              <p>
                From state-of-the-art equipment to personalized training programs and nutrition plans, we're here to
                support you every step of the way.
              </p>
            </div>
          </Col>
        </Row>
        <Row className="justify-content-center align-items-center">
          <Col lg="8">
            <div className="cta">
              <p>Ready to get started?</p>
              <Link to="/login" className="btn btn-primary">
                Explore Now
              </Link>
              <p>Visit our products</p>
              <Link to="/product" className="btn btn-primary">
                Products
              </Link>
            </div>
          </Col>
        </Row>
        <Row className="justify-content-center align-items-center">
          <Col lg="8">
            <div className="links">
              <div className="link-container">
                <p>Category</p>
                <Link to="/category" className="btn btn-primary">
                  Go to Category
                </Link>
              </div>
              <div className="link-container">
                <p>Gym</p>
                <Link to="/gym" className="btn btn-primary">
                  Go to Gym
                </Link>
              </div>
              <div className="link-container">
                <p>Invoice</p>
                <Link to="/invoice" className="btn btn-primary">
                  Go to Invoice
                </Link>
              </div>
              <div className="link-container">
                <p>Order</p>
                <Link to="/order" className="btn btn-primary">
                  Go to Order
                </Link>
              </div>
              <div className="link-container">
                <p>Order Unit</p>
                <Link to="/order-unit" className="btn btn-primary">
                  Go to Order Unit
                </Link>
              </div>
              <div className="link-container">
                <p>Payment</p>
                <Link to="/payment" className="btn btn-primary">
                  Go to Payment
                </Link>
              </div>
              <div className="link-container">
                <p>Period</p>
                <Link to="/period" className="btn btn-primary">
                  Go to Period</Link>
              </div>
              <div className="link-container">
                <p>Periodic Subscription</p>
                <Link to="/periodic-subscription" className="btn btn-primary">
                  Go to Periodic Subscription
                </Link>
              </div>
              <div className="link-container">
                <p>Plan</p>
                <Link to="/plan" className="btn btn-primary">
                  Go to Plan
                </Link>
              </div>
              <div className="link-container">
                <p>Product History</p>
                <Link to="/product-history" className="btn btn-primary">
                  Go to Product History
                </Link>
              </div>
              {/* Add more link containers as needed */}
            </div>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Home;
